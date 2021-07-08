package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.common.core.util.CollectUtil;
import com.chebianjie.common.core.util.NumberUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.domain.enums.*;
import com.chebianjie.datacleaning.repository.*;
import com.chebianjie.datacleaning.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhengdayue
 * @date: 2021-06-28
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerBillServiceImpl implements ConsumerBillService {

    @Autowired
    private ConsumerBillRepository consumerBillRepository;

    @Autowired
    private ConsumerBillChangeDetailRepository consumerBillChangeDetailRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerLogService consumerLogService;

    @Autowired
    private ConsumerBalanceService consumerBalanceService;

    @Autowired
    private ChargeLogService chargeLogService;

    @Autowired
    private UtUserTotalFlowService utUserTotalFlowService;

    @Autowired
    private BillLogService billLogService;

    @Autowired
    private DataSynTimeRepository dataSynTimeRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConsumerBillSaveService consumerBillSaveService;

    @Autowired
    private ConsumerBillDetailSaveService consumerBillDetailSaveService;

    @Autowired
    private DataSynTimeService dataSynTimeService;

    @Autowired
    private FlowLogService flowLogService;

    @Override
    public void cleanOne(int pageNumber, int pageSize) {
        List<Consumer> consumers = consumerService.findAllByPage(pageNumber * pageSize, pageSize);
        for (Consumer consumer : consumers) {
            rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_FIRST_BILL_ROUTING_KEY, consumer.getId());
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void deleteFail() {
        List<BillLog> billLogs = billLogService.findByStatus(0);
        List<String> unionAccount = billLogs.stream().map(BillLog::getUnionAccount).collect(Collectors.toList());
        List<ConsumerBill> consumerBills = consumerBillRepository.findAllByUnionAccountIn(unionAccount);
        List<String> billIdentifies = consumerBills.stream().map(ConsumerBill::getBillIdentify).collect(Collectors.toList());
        consumerBillChangeDetailRepository.deleteAllByBillIdentifyIn(billIdentifies);
        consumerBillRepository.deleteAll(consumerBills);
    }

    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_FIRST_BILL_QUEUE, containerFactory = "multiListenerContainer")
    public void threadClean(Long id, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        log.info("监听消息,用户id:{},tag:{}",id,tag);
        Consumer consumer = consumerService.findById(id);
        try {
            //过滤重复清洗
            Boolean repeatClean = billLogService.repeatClean(consumer.getUnionAccount());
            if (Objects.equals(repeatClean, true)) {
                channel.basicAck(tag, false);
                return;
            }
            ConsumerLog consumerLog = consumerLogService.findOneByConsumerId(consumer.getId(),1);
            if (consumerLog == null) {
                log.error("用户查询不到迁移log,consumerId:{}", consumer.getId());
                billLogService.save(consumer.getUnionAccount(), 0);
                throw new InvalidParameterException("用户查询不到迁移log");
            }
            List<ConsumerBalance> consumerBalances = consumerBalanceService.findByUnionAccount(consumer.getUnionAccount());
            if (CollectUtil.collectionIsEmpty(consumerBalances)) {
                log.error("用户余额为空,consumerId:{}", consumer.getId());
                billLogService.save(consumer.getUnionAccount(), 0);
                throw new InvalidParameterException("用户余额为空");
            }
            List<UtUserTotalFlow> flows = new ArrayList<>(16);
            if (consumerLog.getCbjId() != null) {
                List<UtUserTotalFlow> cbjFlows = utUserTotalFlowService.cbjFindAllByUid(consumerLog.getCbjId());
                if (CollectUtil.collectionNotEmpty(cbjFlows)) {
                    cbjFlows.forEach(cbjFlow -> cbjFlow.setPlatform(Platform.CHEBIANJIE));
                    flows.addAll(cbjFlows);
                }
                cbjFlows = null;
            }
            if (consumerLog.getChjId() != null) {
                List<UtUserTotalFlow> chjFlows = utUserTotalFlowService.chjFindAllByUid(consumerLog.getChjId());
                if (CollectUtil.collectionNotEmpty(chjFlows)) {
                    chjFlows.forEach(chjFlow -> chjFlow.setPlatform(Platform.CHEHUIJIE));
                    flows.addAll(chjFlows);
                }
                chjFlows = null;
            }
            if (CollectUtil.collectionIsEmpty(flows)) {
                //log.error("用户无流水,consumerId：{}", consumer.getId());
                billLogService.save(consumer.getUnionAccount(), 1);
                //用户无流水
                channel.basicAck(tag, false);
                return;
            }
            //部分时间从秒转毫秒
            flows.forEach(item -> {
                if (isSencond(item.getCreateTime())) {
                    item.setCreateTime(item.getCreateTime() * 1000);
                }
            });
            flows.sort(Comparator.comparing(UtUserTotalFlow::getCreateTime).reversed());
            Map<BalanceType, ConsumerBalance> balanceMap = consumerBalances.stream().collect(Collectors.toMap(ConsumerBalance::getBalanceType, Function.identity()));
            Integer consumerBalance = getBalance(balanceMap.get(BalanceType.REAL_BALANCE));
            Integer consumerGiveBalance = getBalance(balanceMap.get(BalanceType.GIVE_BALANCE));
            updateConsumerBalance(consumer.getUnionAccount(), BalanceType.REAL_BALANCE, consumerBalance);
            updateConsumerBalance(consumer.getUnionAccount(), BalanceType.GIVE_BALANCE, consumerGiveBalance);
            for (UtUserTotalFlow currentFlow : flows) {
                if (currentFlow.getAgentId() != null) {
                    log.info("大客户流水,跳过flow：{}", currentFlow);
                    continue;
                }
                //清洗流水
                ConsumerBill consumerBill = fillInfoConsumerBill(currentFlow, consumer);
                handleBillDetail(consumerBill, currentFlow);
                flowLogService.save(currentFlow.getId(), consumerBill.getPlatform(), 1);
            }
            billLogService.save(consumer.getUnionAccount(), 1);
            flows = null;
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("清洗流水异常e：", e);
            billLogService.save(consumer.getUnionAccount(), 0);
            channel.basicReject(tag, false);
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void addBatchClean(Consumer consumer) {

    }

    private void updateConsumerBalance(String unionAccount, BalanceType balanceType, Integer value) {
        //account+balanceType用户余额、赠送余额
        redisTemplate.opsForValue().set(unionAccount + balanceType, NumberUtil.toString(NumberUtil.getIfNull(value)), 1, TimeUnit.MINUTES);
    }

    private Integer getConsumerBalance(String unionAccount, BalanceType balanceType) {
        return NumberUtil.toInteger(redisTemplate.opsForValue().get(unionAccount + balanceType));
    }

    public void handleBillDetail(ConsumerBill consumerBill, UtUserTotalFlow flow) {
        if (isBalanceChange(consumerBill)) {
            String unionAccount = consumerBill.getUnionAccount();
            Integer balance = getConsumerBalance(unionAccount, BalanceType.REAL_BALANCE);
            Integer giveBalance = getConsumerBalance(unionAccount, BalanceType.GIVE_BALANCE);
            Integer changeBalance = NumberUtil.addIfNull(flow.getNewBalance(), NumberUtil.negativeIfNull(flow.getOldBalance()));
            Integer changeGiveBalance = NumberUtil.addIfNull(flow.getNewGiveBalance(), NumberUtil.negativeIfNull(flow.getOldGiveBalance()));
            ConsumerBillChangeDetail balanceBillDetail = fillInfoChangeDetail(consumerBill.getBillIdentify(), balance, changeBalance, BalanceType.REAL_BALANCE, consumerBill.getPlatform());
            ConsumerBillChangeDetail giveBalanceBillDetail = fillInfoChangeDetail(consumerBill.getBillIdentify(), giveBalance, changeGiveBalance, BalanceType.GIVE_BALANCE, consumerBill.getPlatform());
            consumerBillDetailSaveService.save(balanceBillDetail);
            consumerBillDetailSaveService.save(giveBalanceBillDetail);
            updateConsumerBalance(unionAccount, BalanceType.REAL_BALANCE, balanceBillDetail.getPreChangeValue());
            updateConsumerBalance(unionAccount, BalanceType.GIVE_BALANCE, giveBalanceBillDetail.getPreChangeValue());
        }
    }

    //余额流水,金额变动
    public Boolean isBalanceChange(ConsumerBill consumerBill) {
        BillType billType = consumerBill.getBillType();
        //普通洗车
        if (billType == BillType.WASH) {
            return consumerBill.getOrderPaymentType() == OrderPaymentType.ORDINARY;
            //充值、商城、退款、门店
        } else if (billType == BillType.CHARGE || billType == BillType.MALL || billType == BillType.REFUND || billType == BillType.STORE) {
            return true;
            //购买消费卡
        }else if(billType == BillType.CONSUMER_CARD) {
            return false;
        }else {
            //增值服务
            return consumerBill.getPaymentMethod() == PaymentMethod.BALANCE;
        }
    }

    public ConsumerBillChangeDetail fillInfoChangeDetail(String billIdentify, Integer afterChangeValue, Integer changeValue, BalanceType balanceType, Platform platform) {
        ConsumerBillChangeDetail detail = new ConsumerBillChangeDetail();
        detail.setBillIdentify(billIdentify);
        detail.setPreChangeValue(NumberUtil.addIfNull(afterChangeValue, NumberUtil.negativeIfNull(changeValue)));
        detail.setChangeValue(changeValue);
        detail.setBalanceType(balanceType);
        detail.setAfterChangeValue(afterChangeValue);
        detail.setPlatform(platform);
        return detail;
    }

    public ConsumerBill fillInfoConsumerBill(UtUserTotalFlow currentFlow, Consumer consumer) {
        String billIdentify = UUID.randomUUID().toString().replaceAll("-", "");
        ConsumerBill consumerBill = new ConsumerBill();
        consumerBill.setPlatform(currentFlow.getPlatform());
        consumerBill.setUnionAccount(consumer.getUnionAccount());
        consumerBill.setBillIdentify(billIdentify);
        consumerBill.setOrderId(currentFlow.getOrderId());
        consumerBill.setOrderNum(currentFlow.getOrderNum());
        consumerBill.setOrderPlatform(currentFlow.getPlatform());
        consumerBill.setCreateTime(toLocalDateTime(currentFlow.getCreateTime()));
        consumerBill.setOrderStartTime(toLocalDateTime(currentFlow.getCreateTime()));
        consumerBill.setOrderEndTime(toLocalDateTime(currentFlow.getCreateTime()));
        consumerBill.setOrderCommented(currentFlow.getWashComment());
        consumerBill.setRemark(currentFlow.getRemark());
        //consumerBill.setVersion();
        consumerBill.setConsumptionLocation(currentFlow.getSiteName());
        consumerBill.setOperationName(currentFlow.getOperationName());
        consumerBill.setWechatnum(currentFlow.getWechatnum());
        consumerBill.setDisplay(true);
        consumerBill.setPaymentAmount(NumberUtil.addIfNull(currentFlow.getNewBalance(), currentFlow.getNewGiveBalance(),
                NumberUtil.negativeIfNull(currentFlow.getOldBalance()), NumberUtil.negativeIfNull(currentFlow.getOldGiveBalance())));
        if (currentFlow.getCouponPrice() != null && !currentFlow.getCouponPrice().equals("0")) {
            consumerBill.setHasDiscount(true);
            consumerBill.setDiscountType(toDiscountType(currentFlow.getCouponType()));
            consumerBill.setDiscountId(currentFlow.getCouponId());
            consumerBill.setDiscountPlatform(currentFlow.getPlatform());
            if (consumerBill.getDiscountType() != null) {
                consumerBill.setDiscountName(consumerBill.getDiscountType().name());
            }
            consumerBill.setDiscountValue(NumberUtil.toInteger(currentFlow.getCouponPrice()));
        } else {
            consumerBill.setHasDiscount(false);
        }
        Integer trxType = currentFlow.getTrxType();
        //充值
        if (trxType == 1) {
            //余额转入
            if (Objects.equals(currentFlow.getTrxExpandType(), 13) && Objects.equals(currentFlow.getTrxExpandStatus(), 64)) {
                consumerBill.setBillType(BillType.REFUND);
                consumerBill.setOrderType(OrderType.BALANCE_COME);
                consumerBill.setOrderStatus(OrderStatus.FINISH);
                consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            } else {
                //普通充值
                consumerBill.setBillType(BillType.CHARGE);
                UtChargeLog chargeLog = getUtChargeLog(currentFlow.getOrderId(), currentFlow.getPlatform());
                consumerBill.setOrderType(tochargeOrderType(chargeLog == null ? null : chargeLog.getChargeType()));
                consumerBill.setOrderStatus(toChargeOrderStatus(chargeLog == null ? null : chargeLog.getStatus()));
                OrderType orderType = consumerBill.getOrderType();
                if (orderType != null) {
                    if (orderType == OrderType.CHARGE_USER || orderType == OrderType.CHARGE_STAFF) {
                        consumerBill.setPaymentMethod(PaymentMethod.WECHAT);
                    } else if (orderType == OrderType.CHARGE_MANAGER) {
                        consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
                    } else if (orderType == OrderType.CHARGE_GIVE) {
                        consumerBill.setPaymentMethod(PaymentMethod.COMMENT_REWARD);
                    }
                }
            }
            //洗车
        } else if (trxType == 2) {
            consumerBill.setBillType(BillType.WASH);
            consumerBill.setOrderType(toWashOrderType(currentFlow.getWashMachineType()));
            consumerBill.setOrderStatus(OrderStatus.FINISH);
            consumerBill.setOrderPaymentType(toWshOrderPaymentType(currentFlow.getWashConsumeType()));
            OrderPaymentType orderPaymentType = consumerBill.getOrderPaymentType();
            if (orderPaymentType != null) {
                if (orderPaymentType == OrderPaymentType.ORDINARY || orderPaymentType == OrderPaymentType.AGENT) {
                    consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
                } else if (orderPaymentType == OrderPaymentType.ONCE) {
                    consumerBill.setPaymentMethod(PaymentMethod.WECHAT);
                }
            }
            //商城消费
        } else if (trxType == 3) {
            consumerBill.setBillType(BillType.MALL);
            consumerBill.setOrderStatus(OrderStatus.FINISH);
            consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            //退款
        } else if (trxType == 4) {
            consumerBill.setBillType(BillType.REFUND);
            consumerBill.setOrderStatus(OrderStatus.CLOSE);
            consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            //全自动故障订单退款
            if (Objects.equals(currentFlow.getTrxExpandType(), 21)) {
                consumerBill.setOrderType(OrderType.AUTO_REFUND);
                //商城退款
            } else if (Objects.equals(currentFlow.getTrxExpandType(), 31)) {
                consumerBill.setOrderType(OrderType.MALL_REFUND);
                //门店消费退款
            } else if (Objects.equals(currentFlow.getTrxExpandType(), 51)) {
                consumerBill.setOrderType(OrderType.STORE_REFUND);
            }
            //门店消费
        } else if (trxType == 5) {
            consumerBill.setBillType(BillType.STORE);
            consumerBill.setOrderStatus(OrderStatus.FINISH);
            //余额清零
        } else if (trxType == 6) {
            consumerBill.setBillType(BillType.REFUND);
            if (Objects.equals(currentFlow.getTrxExpandStatus(), 63)) {
                consumerBill.setOrderType(OrderType.BALANCE_TRANSFER);
                consumerBill.setOrderStatus(OrderStatus.REFUND_AGREE);
                consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            } else if (Objects.equals(currentFlow.getTrxExpandStatus(), 61)) {
                consumerBill.setOrderType(OrderType.RESET);
                consumerBill.setOrderStatus(OrderStatus.REFUND_APPLY);
            } else if (Objects.equals(currentFlow.getTrxExpandStatus(), 62)) {
                consumerBill.setOrderType(OrderType.RESET);
                consumerBill.setOrderStatus(OrderStatus.REFUND_AGREE);
            } else {
                consumerBill.setOrderType(OrderType.RESET);
                consumerBill.setOrderStatus(OrderStatus.REFUND_AGREE);
            }
            //旧余额转移数据
        } else if (trxType == 7) {
            consumerBill.setBillType(BillType.REFUND);
            consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            //充值
            if (Objects.equals(currentFlow.getFundDirection(), 1)) {
                consumerBill.setOrderType(OrderType.BALANCE_COME);
                consumerBill.setOrderStatus(OrderStatus.FINISH);
                //消费
            } else if (Objects.equals(currentFlow.getFundDirection(), 2)) {
                consumerBill.setOrderType(OrderType.BALANCE_TRANSFER);
                consumerBill.setOrderStatus(OrderStatus.REFUND_AGREE);
            }
            //撤销退款
        } else if (trxType == 8) {
            consumerBill.setBillType(BillType.REFUND);
            consumerBill.setOrderType(OrderType.CANCEL_REFUND);
            consumerBill.setOrderStatus(OrderStatus.FINISH);
            consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            //购买年卡
        } else if (trxType == 9) {
            consumerBill.setBillType(BillType.CONSUMER_CARD);
            consumerBill.setOrderStatus(OrderStatus.FINISH);
            consumerBill.setBusinessId(NumberUtil.toString(currentFlow.getUserConsumeCardId()));
            //增值服务
        } else if (trxType == 10) {
            consumerBill.setBillType(BillType.SERVER);
            Integer trxExpandStatus = currentFlow.getTrxExpandStatus();
            consumerBill.setOrderStatus(toServerOrderStatus(currentFlow.getServerOrderStatus()));
            if (Objects.equals(trxExpandStatus, 101)) {
                consumerBill.setOrderStatus(OrderStatus.SERVER_CANCEL_REFUNDING);
            } else if (Objects.equals(trxExpandStatus, 102)) {
                consumerBill.setOrderStatus(OrderStatus.SERVER_CANCEL_REFUND);
            }
            if (Objects.equals(currentFlow.getTrxExpandType(), 12)) {
                consumerBill.setPaymentMethod(PaymentMethod.WECHAT);
            } else {
                consumerBill.setPaymentMethod(PaymentMethod.BALANCE);
            }
        }
        consumerBillSaveService.save(consumerBill);
        return consumerBill;
    }

    private OrderPaymentType toWshOrderPaymentType(Integer washConsumeType) {
        if (washConsumeType != null) {
            if (washConsumeType == 1) {
                return OrderPaymentType.ORDINARY;
            } else if (washConsumeType == 3) {
                return OrderPaymentType.AGENT;
            } else if (washConsumeType == 4) {
                return OrderPaymentType.ONCE;
            } else if (washConsumeType == 5) {
                return OrderPaymentType.SINOPEC;
            } else if (washConsumeType == 6) {
                return OrderPaymentType.CONSUMERCARD;
            }
        }
        return null;
    }

    private OrderType toWashOrderType(Integer washMachineType) {
        if (washMachineType != null) {
            if (washMachineType == 0) {
                return OrderType.SELF_WASH;
            } else if (washMachineType == 1) {
                return OrderType.DUST;
            } else if (washMachineType == 2) {
                return OrderType.AUTO_WASH;
            }
        }
        return null;
    }

    //1支付成功，2待支付，3支付失败，4用户取消支付，5已完成
    private OrderStatus toChargeOrderStatus(Integer value) {
        if (value != null) {
            if (value == 1) {
                return OrderStatus.CHARGE_SUCCESS;
            } else if (value == 2) {
                return OrderStatus.CHARGE_READY;
            } else if (value == 3) {
                return OrderStatus.CHARGE_FAIL;
            } else if (value == 4) {
                return OrderStatus.CHARGE_CANCEL;
            } else if (value == 5) {
                return OrderStatus.CHARGE_FINISH;
            }
        }
        return null;
    }

    private UtChargeLog getUtChargeLog(Long orderId, Platform platform) {
        if (platform == Platform.CHEBIANJIE) {
            return chargeLogService.cbjFindById(orderId);
        } else if (platform == Platform.CHEHUIJIE) {
            return chargeLogService.chjFindById(orderId);
        }
        return null;
    }

    //1用户充值、2是员工（代用户）充值、3是后台充值、4注册赠送，5员工代充（ic卡）
    private OrderType tochargeOrderType(Integer value) {
        if (value != null) {
            if (value == 1) {
                return OrderType.CHARGE_USER;
            } else if (value == 2) {
                return OrderType.CHARGE_STAFF;
            } else if (value == 3) {
                return OrderType.CHARGE_MANAGER;
            } else if (value == 4) {
                return OrderType.CHARGE_GIVE;
            } else if (value == 5) {
                return OrderType.CHARGE_IC_CARD;
            }
        }
        return null;
    }

    private Boolean isSencond(Long createTime) {
        if (createTime != null && createTime != 0) {
            String time = createTime + "";
            return time.length() == 10;
        }
        return false;
    }

    private Integer getBalance(ConsumerBalance consumerBalance) {
        return consumerBalance != null ? consumerBalance.getValue() : 0;
    }

    private static LocalDateTime toLocalDateTime(Long mills) {
        return Instant.ofEpochMilli(mills).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }


    private static DiscountType toDiscountType(Integer couponType) {
        if (couponType != null) {
            if (couponType == 1) {
                return DiscountType.CASH;
            } else if (couponType == 2) {
                return DiscountType.REDUCTION;
            } else if (couponType == 3) {
                return DiscountType.DISCOUNT;
            } else if (couponType == 4) {
                return DiscountType.CAR;
            } else if (couponType == 5) {
                return DiscountType.SINOPEC;
            }
        }
        return null;
    }

    //车服订单进行状态，0待完成-待验证、1待完成-已验证、2超时未验证、3已完成
    private OrderStatus toServerOrderStatus(Integer value) {
        if (value != null) {
            if (value == 0) {
                return OrderStatus.SERVER_NO_CHECK;
            } else if (value == 1) {
                return OrderStatus.SERVER_CHECK;
            } else if (value == 2) {
                return OrderStatus.SERVER_TIME_OUT;
            } else if (value == 3) {
                return OrderStatus.SERVER_FINISH;
            }
        }
        return null;
    }

    @DataSource(name = DataSourcesType.USERPLATFORM)
    private ConsumerBill save(ConsumerBill consumerBill) {
        return consumerBillRepository.save(consumerBill);
    }

    @DataSource(name = DataSourcesType.USERPLATFORM)
    private ConsumerBillChangeDetail save(ConsumerBillChangeDetail consumerBillChangeDetail) {
        return consumerBillChangeDetailRepository.save(consumerBillChangeDetail);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void consumerBillJob() {
        DataSynTime dataSynTime = dataSynTimeRepository.findBySynType(1);
        LocalDateTime timeFrom = dataSynTime.getLastTime();
        LocalDateTime timeTo = LocalDateTime.now().minus(Duration.ofSeconds(5));

        //同步完发到消息队列
        UtUserTotalFlow message = new UtUserTotalFlow();
        rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_BILL_ROUTING_KEY, message);

        dataSynTime.setLastTime(timeTo);
        dataSynTimeService.updateDataSynTime(dataSynTime);
    }
}
