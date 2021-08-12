package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.common.core.util.CollectUtil;
import com.chebianjie.common.core.util.NumberUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.domain.enums.*;
import com.chebianjie.datacleaning.dto.FirstBillBatchMessage;
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
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DataSynTimeService dataSynTimeService;

    @Autowired
    private BatchSaveService batchSaveService;

    @Autowired
    private FailConsumerBillLogRepository failConsumerBillLogRepository;

    @Autowired
    private FailConsumerBillLogService failConsumerBillLogService;

    @Autowired
    protected CbjUtConsumerService cbjUtConsumerService;

    @Autowired
    protected ChjUtConsumerService chjUtConsumerService;

    @Override
    public void cleanOne(int pageNumber, int pageSize) {
        List<Consumer> consumers = consumerService.findAllByPage(pageNumber * pageSize, pageSize);
        List<Long> consumerIds = consumers.stream().map(Consumer::getId).collect(Collectors.toList());
        //每次发送100个用户
        List<List<Long>> collections = splitList(consumerIds, 100);
        collections.forEach(item->{
            FirstBillBatchMessage firstBillBatchMessage = new FirstBillBatchMessage();
            firstBillBatchMessage.setIds(item);
            rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_FIRST_BILL_ROUTING_KEY, firstBillBatchMessage);
        });
    }

    /**
     * 对集合进行切割,按照入参size进行切割
     *
     * @param lists 待切割集合
     * @param size  切割大小
     * @param <T>   泛型T
     * @return 切割完后的集合
     */
    public static <T> List<List<T>> splitList(Collection<T> lists, int size) {
        int splitSize = (lists.size() + size - 1) / size;
        return Stream.iterate(0, n -> n + 1).limit(splitSize).parallel().map(index -> lists.stream().skip(index * size).limit(size).parallel().collect(Collectors.toList())).collect(Collectors.toList());
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
    public void threadClean(FirstBillBatchMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        //批量查出100个用户
        List<Consumer> consumers = consumerService.findAllByIdIn(message.getIds());
        List<String> consumerUnionAccounts = consumers.stream().map(Consumer::getUnionAccount).collect(Collectors.toList());
        Map<String, Boolean> repeatCleanMap = billLogService.batchRepeatClean(consumerUnionAccounts);
        List<ConsumerLog> consumerLogs = consumerLogService.findAllByConsumerIdInAndType(message.getIds(), 1);
        Map<Long, ConsumerLog> consumerLogMap = consumerLogs.stream().collect(Collectors.toMap(ConsumerLog::getConsumerId, Function.identity()));
        Map<String, List<ConsumerBalance>> consumerBalanceMap = consumerBalanceService.batchFindByUnionAccount(consumerUnionAccounts);
        try {
            List<ConsumerBill> batchConsumerBills = new ArrayList<>(5000);
            List<ConsumerBillChangeDetail> batchConsumerBillChangeDetails = new ArrayList<>(10000);
            List<FlowLog> batchFlowLogs = new ArrayList<>(5000);
            //Instant now1 = Instant.now();
            for (Consumer consumer : consumers) {
                //过滤重复清洗
                Boolean repeatClean = repeatCleanMap.get(consumer.getUnionAccount());
                if (Objects.equals(repeatClean, true)) {
                    //channel.basicAck(tag, false);
                    //return;
                    continue;
                }
                ConsumerLog consumerLog = consumerLogMap.get(consumer.getId());
                if (consumerLog == null) {
                    log.error("用户查询不到迁移log,consumerId:{}", consumer.getId());
                    billLogService.save(consumer.getUnionAccount(), 0);
                    throw new InvalidParameterException("用户查询不到迁移log");
                }
                List<ConsumerBalance> consumerBalances = consumerBalanceMap.get(consumer.getUnionAccount());
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
                }
                if (consumerLog.getChjId() != null) {
                    List<UtUserTotalFlow> chjFlows = utUserTotalFlowService.chjFindAllByUid(consumerLog.getChjId());
                    if (CollectUtil.collectionNotEmpty(chjFlows)) {
                        chjFlows.forEach(chjFlow -> chjFlow.setPlatform(Platform.CHEHUIJIE));
                        flows.addAll(chjFlows);
                    }
                }
                if (CollectUtil.collectionIsEmpty(flows)) {
                    //log.error("用户无流水,consumerId：{}", consumer.getId());
                    //billLogService.save(consumer.getUnionAccount(), 1);
                    //用户无流水
                    //channel.basicAck(tag, false);
                    //return;
                    continue;
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
                List<ConsumerBill> consumerBills = new ArrayList<>(flows.size());
                List<ConsumerBillChangeDetail> consumerBillChangeDetails = new ArrayList<>(flows.size() * 2);
                List<FlowLog> flowLogs = new ArrayList<>(flows.size());
                for (UtUserTotalFlow currentFlow : flows) {
                    if (currentFlow.getAgentId() != null) {
                        //log.info("大客户流水,跳过flow：{}", currentFlow);
                        continue;
                    }
                    //清洗流水
                    ConsumerBill consumerBill = fillInfoConsumerBill(currentFlow, consumer);
                    List<ConsumerBillChangeDetail> consumerBillChangeDetailTemps = handleBillDetail(consumerBill, currentFlow);
                    FlowLog flowLog = new FlowLog(currentFlow.getId(), consumerBill.getPlatform(), 1);
                    consumerBills.add(consumerBill);
                    if (CollectUtil.collectionNotEmpty(consumerBillChangeDetailTemps)) {
                        consumerBillChangeDetails.addAll(consumerBillChangeDetailTemps);
                    }
                    flowLogs.add(flowLog);
                }
                batchConsumerBills.addAll(consumerBills);
                batchConsumerBillChangeDetails.addAll(consumerBillChangeDetails);
                batchFlowLogs.addAll(flowLogs);
            }
            //Instant end1 = Instant.now();
            //log.info("清洗步骤1：{}s", Duration.between(now1, end1).toMillis()/1000);
            //Instant now2 = Instant.now();
            batchSaveService.firstBatchSaveAll(batchConsumerBills, batchConsumerBillChangeDetails, batchFlowLogs);
            billLogService.saveAll(consumerUnionAccounts, 1);
            //Instant end2 = Instant.now();
            //log.info("清洗步骤2：{}s", Duration.between(now2, end2).toMillis()/1000);
            //log.info("清洗步骤3,总时间：{}s",Duration.between(now1,end2).toMillis()/1000);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("清洗流水异常，用户id集:{}，erromessage:{}", message.getIds().toArray(), e.getMessage());
            billLogService.saveAll(consumerUnionAccounts, 0);
            channel.basicReject(tag, false);
        }
    }

    private void updateConsumerBalance(String unionAccount, BalanceType balanceType, Integer value) {
        //account+balanceType用户余额、赠送余额
        redisTemplate.opsForValue().set(unionAccount + balanceType, NumberUtil.toString(NumberUtil.getIfNull(value)), 10, TimeUnit.SECONDS);
    }

    private Integer getConsumerBalance(String unionAccount, BalanceType balanceType) {
        return NumberUtil.toInteger(redisTemplate.opsForValue().get(unionAccount + balanceType));
    }

    public List<ConsumerBillChangeDetail> handleBillDetail(ConsumerBill consumerBill, UtUserTotalFlow flow) {
        if (isBalanceChange(consumerBill)) {
            String unionAccount = consumerBill.getUnionAccount();
            Integer balance = getConsumerBalance(unionAccount, BalanceType.REAL_BALANCE);
            Integer giveBalance = getConsumerBalance(unionAccount, BalanceType.GIVE_BALANCE);
            Integer changeBalance = NumberUtil.addIfNull(flow.getNewBalance(), NumberUtil.negativeIfNull(flow.getOldBalance()));
            Integer changeGiveBalance = NumberUtil.addIfNull(flow.getNewGiveBalance(), NumberUtil.negativeIfNull(flow.getOldGiveBalance()));
            ConsumerBillChangeDetail balanceBillDetail = fillInfoChangeDetail(consumerBill.getBillIdentify(), balance, changeBalance, BalanceType.REAL_BALANCE, consumerBill.getPlatform());
            ConsumerBillChangeDetail giveBalanceBillDetail = fillInfoChangeDetail(consumerBill.getBillIdentify(), giveBalance, changeGiveBalance, BalanceType.GIVE_BALANCE, consumerBill.getPlatform());
            List<ConsumerBillChangeDetail> consumerBillChangeDetails = new ArrayList<>(2);
            consumerBillChangeDetails.add(balanceBillDetail);
            consumerBillChangeDetails.add(giveBalanceBillDetail);
            updateConsumerBalance(unionAccount, BalanceType.REAL_BALANCE, balanceBillDetail.getPreChangeValue());
            updateConsumerBalance(unionAccount, BalanceType.GIVE_BALANCE, giveBalanceBillDetail.getPreChangeValue());
            return consumerBillChangeDetails;
        }
        return null;
    }

    public List<ConsumerBillChangeDetail> handleFailBillDetail(ConsumerBill consumerBill, UtUserTotalFlow flow) {
        if (isBalanceChange(consumerBill)) {
            String unionAccount = consumerBill.getUnionAccount();
            Integer balance = getConsumerBalance(unionAccount, BalanceType.REAL_BALANCE);
            Integer giveBalance = getConsumerBalance(unionAccount, BalanceType.GIVE_BALANCE);
            Integer changeBalance = NumberUtil.addIfNull(flow.getNewBalance(), NumberUtil.negativeIfNull(flow.getOldBalance()));
            Integer changeGiveBalance = NumberUtil.addIfNull(flow.getNewGiveBalance(), NumberUtil.negativeIfNull(flow.getOldGiveBalance()));
            ConsumerBillChangeDetail balanceBillDetail = fillInfoFailChangeDetail(consumerBill.getBillIdentify(), balance, changeBalance, BalanceType.REAL_BALANCE, consumerBill.getPlatform());
            ConsumerBillChangeDetail giveBalanceBillDetail = fillInfoFailChangeDetail(consumerBill.getBillIdentify(), giveBalance, changeGiveBalance, BalanceType.GIVE_BALANCE, consumerBill.getPlatform());
            List<ConsumerBillChangeDetail> consumerBillChangeDetails = new ArrayList<>(2);
            consumerBillChangeDetails.add(balanceBillDetail);
            consumerBillChangeDetails.add(giveBalanceBillDetail);
            updateConsumerBalance(unionAccount, BalanceType.REAL_BALANCE, balanceBillDetail.getPreChangeValue());
            updateConsumerBalance(unionAccount, BalanceType.GIVE_BALANCE, giveBalanceBillDetail.getPreChangeValue());
            return consumerBillChangeDetails;
        }
        return null;
    }

    //余额流水,金额变动
    public Boolean isBalanceChange(ConsumerBill consumerBill) {
        BillType billType = consumerBill.getBillType();
        //普通洗车/未启动机器代洗(保存余额变更无变更。)
        if (billType == BillType.WASH) {
            return (consumerBill.getOrderPaymentType() == OrderPaymentType.ORDINARY && consumerBill.getOrderStatus() == OrderStatus.FINISH);
            //充值、商城、退款、门店
        } else if (billType == BillType.CHARGE || billType == BillType.MALL || billType == BillType.REFUND || billType == BillType.STORE) {
            if (billType == BillType.CHARGE && consumerBill.getOrderStatus() == OrderStatus.CHARGE_READY) {
                return false;
            }
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
        detail.setPreChangeValue(afterChangeValue);
        detail.setChangeValue(changeValue);
        detail.setBalanceType(balanceType);
        detail.setAfterChangeValue(NumberUtil.addIfNull(afterChangeValue, changeValue));
        detail.setPlatform(platform);
        return detail;
    }

    public ConsumerBillChangeDetail fillInfoFailChangeDetail(String billIdentify, Integer afterChangeValue, Integer changeValue, BalanceType balanceType, Platform platform) {
        ConsumerBillChangeDetail detail = new ConsumerBillChangeDetail();
        detail.setBillIdentify(billIdentify);
        detail.setPreChangeValue(afterChangeValue - NumberUtil.getIfNull(changeValue));
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
            //未启动机器
            if (Objects.equals(currentFlow.getTrxExpandType(), 23)) {
                consumerBill.setOrderStatus(OrderStatus.UNSTART);
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
            consumerBill.setPaymentAmount(currentFlow.getAmount());
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
        //consumerBillSaveService.save(consumerBill);
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
            } else if (washConsumeType == 7) {
                return OrderPaymentType.SERVER;
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
        DataSynTime dataSynTime = dataSynTimeService.findBySynType(1);
        Long timeFrom = dataSynTime.getLastTime();
        LocalDateTime timeTo = LocalDateTime.now().minus(Duration.ofSeconds(5));
        int pageSize = 1000;
        //车便捷分页
        int cbjTotal = utUserTotalFlowService.cbjCountByCreateTimeBetween(timeFrom, toEpochMilli(timeTo));
        int cbjTotalPage = computeTotalPage(cbjTotal);
        for (int pageNumber = 0; pageNumber <= cbjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtUserTotalFlow> cbjFlows = utUserTotalFlowService.cbjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            cbjFlows.forEach(message -> {
                message.setPlatform(Platform.CHEBIANJIE);
                rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_BILL_ROUTING_KEY, message);
            });
            Instant end = Instant.now();
            log.info("车便捷用户流水增量清洗,总页数:{},第：{}页,总用时：{} s", cbjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }

        DataSynTime chjDataSynTime = dataSynTimeService.findBySynType(3);
        Long chjTimeFrom = chjDataSynTime.getLastTime();
        //车惠捷分页
        int chjTotal = utUserTotalFlowService.chjCountByCreateTimeBetween(chjTimeFrom, toEpochMilli(timeTo));
        int chjTotalPage = computeTotalPage(chjTotal);
        for (int pageNumber = 0; pageNumber <= chjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtUserTotalFlow> chjFlows = utUserTotalFlowService.chjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            chjFlows.forEach(message -> {
                message.setPlatform(Platform.CHEHUIJIE);
                rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_BILL_ROUTING_KEY, message);
            });
            Instant end = Instant.now();
            log.info("车惠捷用户流水增量清洗,总页数:{},第：{}页,总用时：{} s", chjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        chjDataSynTime.setLastTime(toEpochMilli(timeTo));
        dataSynTime.setLastTime(toEpochMilli(timeTo));
        dataSynTimeService.updateDataSynTime(dataSynTime);
        dataSynTimeService.updateDataSynTime(chjDataSynTime);
        log.info("同步增量,车便捷流水增量cbjTotal：{}，车惠捷流水增量chjTotal：{}，起始时间timeFrom：{}，截止时间timeTo：{}", cbjTotal, chjTotal, timeFrom, timeTo);
    }

    private Long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    private int computeTotalPage(long total) {
        int pageSize = 1000;
        int totalPage = (int)total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        return totalPage;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void cleanOneConsumer(Long id) {
        Consumer consumer = consumerService.findById(id);
        rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_FIRST_BILL_ROUTING_KEY, consumer.getId());
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void handleFail() {
        List<BillLog> billLogs = billLogService.findByStatus(0);
        List<String> unionAccounts = billLogs.stream().map(BillLog::getUnionAccount).collect(Collectors.toList());
        List<Long> ids = consumerService.findByUnionAccountIn(unionAccounts).stream().map(Consumer::getId).collect(Collectors.toList());
        FirstBillBatchMessage firstBillBatchMessage = new FirstBillBatchMessage();
        firstBillBatchMessage.setIds(ids);
        rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_EXCHANGE, RabbitMqConstants.DATA_CLEAN_FIRST_BILL_ROUTING_KEY, firstBillBatchMessage);
        billLogService.deleteAll(billLogs);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerBill findAllByUnionAccount(String unionAccount) {
        return consumerBillRepository.findAllByUnionAccountSql(unionAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void handleFailDetail() {
        List<FailConsumerBillLog> all = failConsumerBillLogService.findAll();
        LocalDateTime time = LocalDateTime.of(2021, Month.JULY, 30, 0, 0, 0);
        for (FailConsumerBillLog failConsumerBillLog : all) {
            List<ConsumerBill> consumerBills = consumerBillRepository.findAllByUnionAccountAndCreateTimeGreaterThanOrderByCreateTimeDesc(failConsumerBillLog.getConsumerAccount(), time);
            Map<String, ConsumerBill> billMap = consumerBills.stream().collect(Collectors.toMap(ConsumerBill::getBillIdentify, Function.identity()));
            List<String> billIdentifies = consumerBills.stream().map(ConsumerBill::getBillIdentify).collect(Collectors.toList());
            List<ConsumerBillChangeDetail> consumerBillChangeDetails = consumerBillChangeDetailRepository.findAllByBillIdentifyIn(billIdentifies);
            Map<String, List<ConsumerBillChangeDetail>> detailMap = consumerBillChangeDetails.stream().collect(Collectors.groupingBy(ConsumerBillChangeDetail::getBillIdentify));
            List<ConsumerBalance> consumerBalances = consumerBalanceService.findByUnionAccount(failConsumerBillLog.getConsumerAccount());
            if (CollectUtil.collectionIsEmpty(consumerBalances)) {
                log.error("用户余额为空,consumerAccount:{}", failConsumerBillLog.getConsumerAccount());
                throw new InvalidParameterException("用户余额为空");
            }
            Map<BalanceType, ConsumerBalance> balanceMap = consumerBalances.stream().collect(Collectors.toMap(ConsumerBalance::getBalanceType, Function.identity()));
            Integer consumerBalance = getBalance(balanceMap.get(BalanceType.REAL_BALANCE));
            Integer consumerGiveBalance = getBalance(balanceMap.get(BalanceType.GIVE_BALANCE));
            updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.REAL_BALANCE, NumberUtil.getIfNull(consumerBalance));
            updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.GIVE_BALANCE, NumberUtil.getIfNull(consumerGiveBalance));
            for (String billIdentify : billIdentifies) {
                ConsumerBill consumerBill = billMap.get(billIdentify);
                //涉及余额变更
                if (isBalanceChange(consumerBill)) {
                    List<ConsumerBillChangeDetail> details = detailMap.get(billIdentify);
                    Integer currentBalance = getConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.REAL_BALANCE);
                    Integer currentGiveBalance = getConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.GIVE_BALANCE);
                    for (ConsumerBillChangeDetail consumerBillChangeDetail : details) {
                        BalanceType balanceType = consumerBillChangeDetail.getBalanceType();
                        if (balanceType == BalanceType.REAL_BALANCE) {
                            consumerBillChangeDetail.setAfterChangeValue(currentBalance);
                            consumerBillChangeDetail.setPreChangeValue(consumerBillChangeDetail.getAfterChangeValue() - consumerBillChangeDetail.getChangeValue());
                            //当前pre为上一笔的after
                            updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.REAL_BALANCE, NumberUtil.getIfNull(consumerBillChangeDetail.getPreChangeValue()));
                        } else if (balanceType == BalanceType.GIVE_BALANCE) {
                            consumerBillChangeDetail.setAfterChangeValue(currentGiveBalance);
                            consumerBillChangeDetail.setPreChangeValue(consumerBillChangeDetail.getAfterChangeValue() - consumerBillChangeDetail.getChangeValue());
                            updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.GIVE_BALANCE, NumberUtil.getIfNull(consumerBillChangeDetail.getPreChangeValue()));
                        }
                        consumerBillChangeDetailRepository.save(consumerBillChangeDetail);
                    }
                }
            }
            log.info("failConsumerBillLog处理完成，id：{}", failConsumerBillLog.getId());
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void handleFailOne(Long id) {
        LocalDateTime time = LocalDateTime.of(2021, Month.JULY, 30, 0, 0, 0);
        FailConsumerBillLog failConsumerBillLog = failConsumerBillLogRepository.findById(id).orElse(null);
        List<ConsumerBill> consumerBills = consumerBillRepository.findAllByUnionAccountAndCreateTimeGreaterThanOrderByCreateTimeDesc(failConsumerBillLog.getConsumerAccount(), time);
        Map<String, ConsumerBill> billMap = consumerBills.stream().collect(Collectors.toMap(ConsumerBill::getBillIdentify, Function.identity()));
        List<String> billIdentifies = consumerBills.stream().map(ConsumerBill::getBillIdentify).collect(Collectors.toList());
        List<ConsumerBillChangeDetail> consumerBillChangeDetails = consumerBillChangeDetailRepository.findAllByBillIdentifyIn(billIdentifies);
        Map<String, List<ConsumerBillChangeDetail>> detailMap = consumerBillChangeDetails.stream().collect(Collectors.groupingBy(ConsumerBillChangeDetail::getBillIdentify));
        List<ConsumerBalance> consumerBalances = consumerBalanceService.findByUnionAccount(failConsumerBillLog.getConsumerAccount());
        if (CollectUtil.collectionIsEmpty(consumerBalances)) {
            log.error("用户余额为空,consumerAccount:{}", failConsumerBillLog.getConsumerAccount());
            throw new InvalidParameterException("用户余额为空");
        }
        Map<BalanceType, ConsumerBalance> balanceMap = consumerBalances.stream().collect(Collectors.toMap(ConsumerBalance::getBalanceType, Function.identity()));
        Integer consumerBalance = getBalance(balanceMap.get(BalanceType.REAL_BALANCE));
        Integer consumerGiveBalance = getBalance(balanceMap.get(BalanceType.GIVE_BALANCE));
        updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.REAL_BALANCE, NumberUtil.getIfNull(consumerBalance));
        updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.GIVE_BALANCE, NumberUtil.getIfNull(consumerGiveBalance));
        for (String billIdentify : billIdentifies) {
            ConsumerBill consumerBill = billMap.get(billIdentify);
            //涉及余额变更
            if (isBalanceChange(consumerBill)) {
                List<ConsumerBillChangeDetail> details = detailMap.get(billIdentify);
                Integer currentBalance = getConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.REAL_BALANCE);
                Integer currentGiveBalance = getConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.GIVE_BALANCE);
                for (ConsumerBillChangeDetail consumerBillChangeDetail : details) {
                    BalanceType balanceType = consumerBillChangeDetail.getBalanceType();
                    if (balanceType == BalanceType.REAL_BALANCE) {
                        consumerBillChangeDetail.setAfterChangeValue(currentBalance);
                        consumerBillChangeDetail.setPreChangeValue(consumerBillChangeDetail.getAfterChangeValue() - consumerBillChangeDetail.getChangeValue());
                        //当前pre为上一笔的after
                        updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.REAL_BALANCE, NumberUtil.getIfNull(consumerBillChangeDetail.getPreChangeValue()));
                    } else if (balanceType == BalanceType.GIVE_BALANCE) {
                        consumerBillChangeDetail.setAfterChangeValue(currentGiveBalance);
                        consumerBillChangeDetail.setPreChangeValue(consumerBillChangeDetail.getAfterChangeValue() - consumerBillChangeDetail.getChangeValue());
                        updateConsumerBalance(failConsumerBillLog.getConsumerAccount(), BalanceType.GIVE_BALANCE, NumberUtil.getIfNull(consumerBillChangeDetail.getPreChangeValue()));
                    }
                    consumerBillChangeDetailRepository.save(consumerBillChangeDetail);
                }
            }
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void fixOldUtUserTotalFlow(String phone) {
        UtConsumer cbjConsumer = cbjUtConsumerService.getUtConsumerByPhone(phone);
        UtConsumer chjConsumer = chjUtConsumerService.getUtConsumerByPhone(phone);
        Consumer consumer = consumerService.findByPhone(phone);
        if (consumer == null) {
            log.error("查找不到用户,手机号：{}", phone);
            throw new InvalidParameterException("查找不到用户！");
        }
        List<UtUserTotalFlow> allFlows = new ArrayList<>(12);
        if (cbjConsumer != null) {
            List<UtUserTotalFlow> cbjFlows = utUserTotalFlowService.cbjFindAllByUid(cbjConsumer.getId());
            if (CollectUtil.collectionNotEmpty(cbjFlows)) {
                cbjFlows.forEach(item -> item.setPlatform(Platform.CHEBIANJIE));
                allFlows.addAll(cbjFlows);
            }
        }
        if (chjConsumer != null) {
            List<UtUserTotalFlow> chjFlows = utUserTotalFlowService.chjFindAllByUid(chjConsumer.getId());
            if (CollectUtil.collectionNotEmpty(chjFlows)) {
                chjFlows.forEach(item -> item.setPlatform(Platform.CHEHUIJIE));
                allFlows.addAll(chjFlows);
            }
        }
        if (CollectUtil.collectionNotEmpty(allFlows)) {
            //部分时间从秒转毫秒
            allFlows.forEach(item -> {
                if (isSencond(item.getCreateTime())) {
                    item.setCreateTime(item.getCreateTime() * 1000);
                }
            });
            allFlows.sort(Comparator.comparing(UtUserTotalFlow::getCreateTime).reversed());
        }
        List<ConsumerBill> consumerBills = consumerBillRepository.findAllByUnionAccount(consumer.getUnionAccount());
        if (CollectUtil.collectionNotEmpty(consumerBills)) {
            List<ConsumerBill> filterConsumerBills = consumerBills.stream().filter(consumerBill -> {
                if (Objects.equals(consumerBill.getBillType(), BillType.CHARGE)) {
                    if (Objects.equals(consumerBill.getOrderStatus(), OrderStatus.CHARGE_READY)) {
                        return false;
                    }
                }
                return true;
            }).sorted(Comparator.comparing(ConsumerBill::getCreateTime)).collect(Collectors.toList());
            List<ConsumerBalance> consumerBalances = consumerBalanceService.findByUnionAccount(consumer.getUnionAccount());
            Map<BalanceType, ConsumerBalance> balanceMap = consumerBalances.stream().collect(Collectors.toMap(ConsumerBalance::getBalanceType, Function.identity()));
            Integer balance = NumberUtil.getIfNull(balanceMap.get(BalanceType.REAL_BALANCE).getValue());
            Integer giveBalcne = NumberUtil.getIfNull(balanceMap.get(BalanceType.GIVE_BALANCE).getValue());
            //已经存在流水
            if (CollectUtil.collectionNotEmpty(filterConsumerBills)) {
                //第一笔
                ConsumerBill consumerBill = filterConsumerBills.get(0);
                List<ConsumerBillChangeDetail> consumerBillChangeDetails = consumerBillChangeDetailRepository.findAllByBillIdentify(consumerBill.getBillIdentify());
                Map<BalanceType, ConsumerBillChangeDetail> detailMap = consumerBillChangeDetails.stream().collect(Collectors.toMap(ConsumerBillChangeDetail::getBalanceType, Function.identity()));
                ConsumerBillChangeDetail balanceDetail = detailMap.get(BalanceType.REAL_BALANCE);
                ConsumerBillChangeDetail giveBalcneDetail = detailMap.get(BalanceType.GIVE_BALANCE);
                balance = NumberUtil.getIfNull(balanceDetail.getAfterChangeValue());
                giveBalcne = NumberUtil.getIfNull(giveBalcneDetail.getAfterChangeValue());
            }
            updateConsumerBalance(consumer.getUnionAccount(), BalanceType.REAL_BALANCE, balance);
            updateConsumerBalance(consumer.getUnionAccount(), BalanceType.GIVE_BALANCE, giveBalcne);
            for (UtUserTotalFlow currentFlow : allFlows) {
                if (currentFlow.getAgentId() != null) {
                    //log.info("大客户流水,跳过flow：{}", currentFlow);
                    continue;
                }
                //清洗流水
                ConsumerBill consumerBill = fillInfoConsumerBill(currentFlow, consumer);
                List<ConsumerBillChangeDetail> consumerBillChangeDetailTemps = handleFailBillDetail(consumerBill, currentFlow);
                consumerBillRepository.save(consumerBill);
                consumerBillChangeDetailRepository.saveAll(consumerBillChangeDetailTemps);
            }
        }
    }
}
