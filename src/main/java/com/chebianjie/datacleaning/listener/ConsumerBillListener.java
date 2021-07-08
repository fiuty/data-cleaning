package com.chebianjie.datacleaning.listener;

import cn.hutool.json.JSONUtil;
import com.chebianjie.common.core.constant.RabbitMqConstants;
import com.chebianjie.common.core.util.CollectUtil;
import com.chebianjie.common.core.util.NumberUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
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
import java.util.List;
import java.util.Map;
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


    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_BILL_QUEUE, containerFactory = "singleListenerContainerManual")
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void conusmeMsg(UtUserTotalFlow message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        log.info("流水清洗,消费时间：{},消息：{}", LocalDateTime.now(), JSONUtil.toJsonStr(message));
        try {
            Consumer consumer = findByConsumer(message);
            ConsumerBill lastConsumerBill = consumerBillRepository.findAllByUnionAccount(consumer.getUnionAccount());
            //第一笔流水
            if (lastConsumerBill == null) {
                handleFirstBillBalance(consumer);

            } else {
                ConsumerBill consumerBill = consumerBillService.fillInfoConsumerBill(message, consumer);
            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("流水清洗,消息：{},处理发生异常e：", JSONUtil.toJsonStr(message), e);
            channel.basicReject(tag, false);
            try {

            } catch (Exception e1) {
                log.error("流水清洗,消息：{},保存异常出现异常e：", JSONUtil.toJsonStr(message), e1);
                channel.basicReject(tag, true);
            }
        }
    }

    private Integer getConsumerBalance(String unionAccount, BalanceType balanceType) {
        return NumberUtil.toInteger(redisTemplate.opsForValue().get(unionAccount + balanceType));
    }

    private void handleFirstBillBalance(Consumer consumer) {
        List<ConsumerBalance> consumerBalances = consumerBalanceService.findByUnionAccount(consumer.getUnionAccount());
        if (CollectUtil.collectionIsEmpty(consumerBalances)) {
            log.error("用户余额为空,consumerId:{}", consumer.getId());
            throw new InvalidParameterException("用户余额为空");
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
        //account+balanceType用户余额、赠送余额
        redisTemplate.opsForValue().set(unionAccount + balanceType, NumberUtil.toString(NumberUtil.getIfNull(value)), 1, TimeUnit.MINUTES);
    }

    public Consumer findByConsumer(UtUserTotalFlow message) {
        Platform platform = message.getPlatform();
        ConsumerLog consumerLog;
        if (platform == Platform.CHEHUIJIE) {
            consumerLog = consumerLogService.getOneByChjIdAndStatusAndType(message.getUid(), 1, 1);
        } else {
            consumerLog = consumerLogService.getOneByCbjIdAndStatusAndType(message.getUid(), 1, 1);
        }
        if (consumerLog == null) {
            log.error("用户查询不到迁移log,platform：{},uid:{}", message.getPlatform(), message.getUid());
            throw new InvalidParameterException("用户查询不到迁移log");
        }
        Consumer consumer = null;
        if (consumerLog.getUnionid() != null) {
            consumer = consumerService.findByWechatUnionId(consumerLog.getUnionid());
        }
        if (consumer == null) {
            if (consumerLog.getCbjAccount() != null) {
                consumer = consumerService.findByPhone(consumerLog.getCbjAccount());
            }
            if (consumer == null) {
                consumer = consumerService.findByPhone(consumerLog.getChjAccount());
            }
        }
        if (consumer == null) {
            log.error("查询不到用户,platform：{},uid:{}", message.getPlatform(), message.getUid());
            throw new InvalidParameterException("查询不到用户");
        }
        return consumer;
    }
}
