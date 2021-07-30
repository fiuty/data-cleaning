package com.chebianjie.datacleaning.listener;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhengdayue
 * @date: 2021-07-30
 */
@Slf4j
@Component
public class FlowListener {

    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_FLOW_QUEUE, containerFactory = "singleListenerContainerManual")
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void consume(UtUserTotalFlow message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {

            //处理成功
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("流水清洗余额异常,消息message:{},e:", message, e);
            //重新归入队列 or 记录失败log后续处理，丢弃消息 channel.basicAck(tag, false);
            channel.basicNack(tag, false, true);
        }
    }

}
