package com.chebianjie.datacleaning.listener;

import cn.hutool.json.JSONUtil;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.FailConsumerBillLog;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.dto.AddBillLogMessage;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.FailConsumerBillLogService;
import com.chebianjie.datacleaning.service.UtUserTotalFlowService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
@Slf4j
@Component
public class AddBillLogListener {

    @Autowired
    private FailConsumerBillLogService failConsumerBillLogService;

    @Autowired
    private UtUserTotalFlowService utUserTotalFlowService;

    @Autowired
    private ConsumerLogService consumerLogService;

    @Autowired
    private ConsumerService consumerService;

    @RabbitListener(queues = RabbitMqConstants.ADD_BILL_LOG_QUEUE, containerFactory = "multiListenerContainer")
    @RabbitHandler
    public void conusmeMsg(AddBillLogMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            UtUserTotalFlow utUserTotalFlow;
            if (message.getPlatform() == Platform.CHEBIANJIE) {
                utUserTotalFlow = utUserTotalFlowService.cbjFindById(message.getUtUserTotalFlowId());
            } else {
                utUserTotalFlow = utUserTotalFlowService.chjFindById(message.getUtUserTotalFlowId());
            }
            ConsumerLog consumerLog = consumerLogService.findOneByCbjIdOrChjId(utUserTotalFlow.getUid(), message.getPlatform());
            if (consumerLog == null) {
                log.error("AddBillLogListener查找不到consumerLog，message:{}", JSONUtil.toJsonStr(message));
                channel.basicAck(tag, false);
                return;
            }
            long consumerId = consumerLog.getConsumerId();
            Consumer consumer = consumerService.findById(consumerId);
            failConsumerBillLogService.save(consumer.getUnionAccount());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("AddBillLogListener异常，message：{}，e:", JSONUtil.toJsonStr(message), e);
            channel.basicAck(tag, false);
        }
    }
}
