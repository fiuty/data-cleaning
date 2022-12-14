package com.chebianjie.datacleaning.listener;

import cn.hutool.json.JSONUtil;
import com.chebianjie.common.core.util.CollectUtil;
import com.chebianjie.common.core.util.NumberUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.domain.enums.BalanceType;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.ConsumerBillRepository;
import com.chebianjie.datacleaning.service.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Slf4j
@Component
public class ConsumerBillListener {

    @Autowired
    private ConsumerLogService consumerLogService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerBillRepository consumerBillRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ConsumerBalanceService consumerBalanceService;

    @Autowired
    private ConsumerBillService consumerBillService;

    @Autowired
    private UtUserTotalFlowService utUserTotalFlowService;

    @Autowired
    private AddBillLogService addBillLogService;

    @Autowired
    private ConsumerBillDetailSaveService consumerBillDetailSaveService;

    @Autowired
    private FlowLogService flowLogService;

    @Autowired
    private BatchSaveService batchSaveService;

    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_BILL_QUEUE, containerFactory = "multiListenerContainer")
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void conusmeMsg(UtUserTotalFlow message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            //????????????
            Boolean flowRepeat = flowLogService.repeatClean(message.getId(), message.getPlatform());
            if (Objects.equals(flowRepeat, true)) {
                channel.basicAck(tag, false);
                return;
            }
            //???????????????????????????
            Boolean repeatClean = addBillLogService.repeatClean(message.getId(), message.getPlatform());
            if (Objects.equals(repeatClean, true)) {
                channel.basicAck(tag, false);
                return;
            }
            ConsumerLog consumerLog = getConsumerLog(message);
            Consumer consumer = getConsumer(message, consumerLog);
            ConsumerBill lastConsumerBill = consumerBillService.findAllByUnionAccount(consumer.getUnionAccount());
            //???????????????
            if (lastConsumerBill == null) {
                handleFirstBillBalance(consumer);
                List<UtUserTotalFlow> flows = getUtUserTotalFlow(consumerLog);
                if (CollectUtil.collectionIsEmpty(flows)) {
                    //log.error("???????????????,consumerId???{}", consumer.getId());
                    //???????????????
                    channel.basicAck(tag, false);
                    return;
                }
                flows.sort(Comparator.comparing(UtUserTotalFlow::getCreateTime).reversed());
                List<ConsumerBill> consumerBills = new ArrayList<>(flows.size());
                List<ConsumerBillChangeDetail> consumerBillChangeDetails = new ArrayList<>(flows.size() * 2);
                List<FlowLog> flowLogs = new ArrayList<>(flows.size());
                List<AddBillLog> addBillLogs = new ArrayList<>(flows.size());
                for (UtUserTotalFlow currentFlow : flows) {
                    //????????????
                    ConsumerBill consumerBill = consumerBillService.fillInfoConsumerBill(currentFlow, consumer);
                    List<ConsumerBillChangeDetail> consumerBillChangeDetailTemps = consumerBillService.handleBillDetail(consumerBill, currentFlow);
                    AddBillLog addBillLog = new AddBillLog(currentFlow.getId(), consumerBill.getPlatform(), 1, null);
                    FlowLog flowLog = new FlowLog(currentFlow.getId(), consumerBill.getPlatform(), 1);
                    consumerBills.add(consumerBill);
                    if (CollectUtil.collectionNotEmpty(consumerBillChangeDetailTemps)) {
                        consumerBillChangeDetails.addAll(consumerBillChangeDetailTemps);
                    }
                    flowLogs.add(flowLog);
                    addBillLogs.add(addBillLog);
                }
                log.debug("addBillLogs???{}", JSONUtil.toJsonStr(addBillLogs));
                batchSaveService.addBatchSaveAll(consumerBills, consumerBillChangeDetails, addBillLogs, flowLogs);
            } else {
                //????????????
                ConsumerBill consumerBill = consumerBillService.fillInfoConsumerBill(message, consumer);
                List<ConsumerBillChangeDetail> consumerBillChangeDetails = new ArrayList<>(2);
                if (consumerBillService.isBalanceChange(consumerBill)) {
                    //?????????????????????
                    String billIdentify = lastConsumerBill.getBillIdentify();
                    List<ConsumerBillChangeDetail> lastBillDetails = consumerBillDetailSaveService.findByBillIdentify(billIdentify);
                    Map<BalanceType, ConsumerBillChangeDetail> lastBalanceMap = lastBillDetails.stream().collect(Collectors.toMap(ConsumerBillChangeDetail::getBalanceType, Function.identity()));
                    Integer balance = getAfterChangeValue(lastBalanceMap.get(BalanceType.REAL_BALANCE));
                    Integer giveBalance = getAfterChangeValue(lastBalanceMap.get(BalanceType.GIVE_BALANCE));
                    Integer changeBalance = NumberUtil.addIfNull(message.getNewBalance(), NumberUtil.negativeIfNull(message.getOldBalance()));
                    Integer changeGiveBalance = NumberUtil.addIfNull(message.getNewGiveBalance(), NumberUtil.negativeIfNull(message.getOldGiveBalance()));
                    ConsumerBillChangeDetail balanceBillDetail = consumerBillService.fillInfoChangeDetail(consumerBill.getBillIdentify(), balance, changeBalance, BalanceType.REAL_BALANCE, consumerBill.getPlatform());
                    ConsumerBillChangeDetail giveBalanceBillDetail = consumerBillService.fillInfoChangeDetail(consumerBill.getBillIdentify(), giveBalance, changeGiveBalance, BalanceType.GIVE_BALANCE, consumerBill.getPlatform());
                    consumerBillChangeDetails.add(balanceBillDetail);
                    consumerBillChangeDetails.add(giveBalanceBillDetail);
                }
                AddBillLog addBillLog = new AddBillLog(message.getId(), consumerBill.getPlatform(), 1, null);
                FlowLog flowLog = new FlowLog(message.getId(), consumerBill.getPlatform(), 1);
                log.debug("addBillLogs???{}", JSONUtil.toJsonStr(addBillLog));
                batchSaveService.addSaveAll(consumerBill,consumerBillChangeDetails,addBillLog,flowLog);
            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("????????????,?????????{},??????????????????erromessage???{}", JSONUtil.toJsonStr(message), e);
            channel.basicReject(tag, false);
            try {
                addBillLogService.save(message.getId(), message.getPlatform(), 0, JSONUtil.toJsonStr(message));
            } catch (Exception e1) {
                log.error("????????????,?????????{},????????????????????????erromessage???{}", JSONUtil.toJsonStr(message), e1.getMessage());
                channel.basicReject(tag, true);
            }
        }
    }

    private Integer getAfterChangeValue(ConsumerBillChangeDetail consumerBillChangeDetail) {
        return NumberUtil.getIfNull(consumerBillChangeDetail.getAfterChangeValue());
    }

    private List<UtUserTotalFlow> getUtUserTotalFlow(ConsumerLog consumerLog) {
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
        return flows;
    }

    private Integer getConsumerBalance(String unionAccount, BalanceType balanceType) {
        return NumberUtil.toInteger(redisTemplate.opsForValue().get(unionAccount + balanceType));
    }

    private void handleFirstBillBalance(Consumer consumer) {
        List<ConsumerBalance> consumerBalances = consumerBalanceService.findByUnionAccount(consumer.getUnionAccount());
        if (CollectUtil.collectionIsEmpty(consumerBalances)) {
            log.error("??????????????????,consumerId:{}", consumer.getId());
            throw new InvalidParameterException("??????????????????");
        }
        Map<BalanceType, ConsumerBalance> balanceMap = consumerBalances.stream().collect(Collectors.toMap(ConsumerBalance::getBalanceType, Function.identity()));
        Integer consumerBalance = getBalance(balanceMap.get(BalanceType.REAL_BALANCE));
        Integer consumerGiveBalance = getBalance(balanceMap.get(BalanceType.GIVE_BALANCE));
        updateConsumerBalance(consumer.getUnionAccount(), BalanceType.REAL_BALANCE, consumerBalance);
        updateConsumerBalance(consumer.getUnionAccount(), BalanceType.GIVE_BALANCE, consumerGiveBalance);
    }

    private Integer getBalance(ConsumerBalance consumerBalance) {
        return consumerBalance != null ? consumerBalance.getValue() : 0;
    }

    private void updateConsumerBalance(String unionAccount, BalanceType balanceType, Integer value) {
        //account+balanceType???????????????????????????
        redisTemplate.opsForValue().set(unionAccount + balanceType, NumberUtil.toString(NumberUtil.getIfNull(value)), 1, TimeUnit.MINUTES);
    }

    private ConsumerLog getConsumerLog(UtUserTotalFlow message) {
        Platform platform = message.getPlatform();
        ConsumerLog consumerLog;
        if (platform == Platform.CHEHUIJIE) {
            consumerLog = consumerLogService.getOneByChjIdAndStatusAndType(message.getUid(), 1, 1);
        } else {
            consumerLog = consumerLogService.getOneByCbjIdAndStatusAndType(message.getUid(), 1, 1);
        }
        if (consumerLog == null) {
            log.error("????????????????????????log,platform???{},uid:{}", message.getPlatform(), message.getUid());
            throw new InvalidParameterException("????????????????????????log");
        }
        return consumerLog;
    }

    public Consumer getConsumer(UtUserTotalFlow message, ConsumerLog consumerLog) {
        Consumer consumer = consumerService.findById(consumerLog.getConsumerId());
        if (consumer == null) {
            log.error("??????????????????,platform???{},uid:{}", message.getPlatform(), message.getUid());
            throw new InvalidParameterException("??????????????????");
        }
        return consumer;
    }
}
