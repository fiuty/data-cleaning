package com.chebianjie.datacleaning.listener;

import cn.hutool.json.JSONUtil;
import com.chebianjie.common.core.constant.RabbitMqConstants;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.UtUserTotalFlowLog;
import com.chebianjie.datacleaning.dto.UtUserTotalFlowMessage;
import com.chebianjie.datacleaning.repository.UtUserTotalFlowLogRepository;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Slf4j
@Component
public class ConsumerBillListener {

    @Autowired
    private UtUserTotalFlowLogRepository utUserTotalFlowLogRepository;

    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_BILL_QUEUE, containerFactory = "singleListenerContainerManual")
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void conusmeMsg(UtUserTotalFlowMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        log.info("流水清洗,消费时间：{},消息：{}", LocalDateTime.now(), JSONUtil.toJsonStr(message));
        try {

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("流水清洗,消息：{},处理发生异常e：", JSONUtil.toJsonStr(message), e);
            channel.basicReject(tag, false);
            try {
                UtUserTotalFlowLog log = new UtUserTotalFlowLog();
                log.setJson(JSONUtil.toJsonStr(message));
                log.setPlatform(message.getPlatform());
                log.setUtUserTotalFlowId(message.getId());
                utUserTotalFlowLogRepository.save(log);
            } catch (Exception e1) {
                log.error("流水清洗,消息：{},保存异常出现异常e：", JSONUtil.toJsonStr(message), e1);
                channel.basicReject(tag, true);
            }
        }
    }
}
