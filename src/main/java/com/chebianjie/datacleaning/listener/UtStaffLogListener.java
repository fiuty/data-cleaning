package com.chebianjie.datacleaning.listener;

import com.chebianjie.common.core.dto.staff.StaffLogDTO;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.dto.StaffLogMessage;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtStaffLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * @author zhengdayue
 * @date: 2021-07-09
 */
@Slf4j
@Component
public class UtStaffLogListener {

    @Autowired
    private ConsumerLogService consumerLogService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private UtStaffLogService utStaffLogService;


    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_FIRST_STAFF_LOG_QUEUE, containerFactory = "multiListenerContainer")
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void firstConusmeMsg(StaffLogMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            ConsumerLog consumerLog = consumerLogService.findOneByCbjIdOrChjId(message.getConsumerId(), message.getPlatform());
            if (consumerLog == null) {
                throw new InvalidParameterException("查找不到consumerLog");
            }
            long consumerId = consumerLog.getConsumerId();
            Consumer consumer = consumerService.findById(consumerId);
            if (consumer == null) {
                throw new InvalidParameterException("查找不到用户consumer");
            }
            if (message.getPlatform() == Platform.CHEBIANJIE) {
                utStaffLogService.updateCbjConsumerAccount(message.getId(), consumer.getUnionAccount());
            } else {
                utStaffLogService.updateChjConsumerAccount(message.getId(), consumer.getUnionAccount());
            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("清洗员工业绩异常,消息message:{},e:", message, e);
            channel.basicAck(tag, false);
        }
    }
}
