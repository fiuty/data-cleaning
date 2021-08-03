package com.chebianjie.datacleaning.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import com.chebianjie.datacleaning.service.ChjUtConsumerService;
import com.chebianjie.datacleaning.service.ConsumerBalanceService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-30
 */
@Slf4j
@Component
public class FlowListener {

    @Autowired
    private CbjUtConsumerService cbjUtConsumerService;

    @Autowired
    private ChjUtConsumerService chjUtConsumerService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerBalanceService consumerBalanceService;

    @RabbitListener(queues = RabbitMqConstants.DATA_CLEAN_FLOW_QUEUE, containerFactory = "singleListenerContainerManual")
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void consume(UtUserTotalFlow message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            //监听流水清洗用户余额 - begin
            UtConsumer curUtConsumer = null;
            //获取utconsumer
            if(message.getPlatform().equals(Platform.CHEBIANJIE)){
                curUtConsumer = cbjUtConsumerService.getUtConsumerById(message.getUid());
            }else if(message.getPlatform().equals(Platform.CHEHUIJIE)){
                curUtConsumer = chjUtConsumerService.getUtConsumerById(message.getUid());
            }
            //判断unionid是否为空
            if(curUtConsumer != null && StrUtil.isNotBlank(curUtConsumer.getPhone())) {
                //获取新合并用户
                Consumer consumer = consumerService.findByPhone(curUtConsumer.getPhone());
                if(consumer != null && StrUtil.isNotBlank(consumer.getPhone())) {
                    //获取车便捷旧用户数据
                    List<UtConsumer> cbjUtConsumerList = cbjUtConsumerService.getUtConsumerListByAccount(consumer.getPhone());
                    UtConsumer cbjUtConsumer = CollectionUtil.isEmpty(cbjUtConsumerList) ? null : cbjUtConsumerList.get(0);
                    //获取车惠捷旧用户数据
                    List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(consumer.getPhone());
                    UtConsumer chjUtConsumer = CollectionUtil.isEmpty(chjUtConsumerList) ? null : chjUtConsumerList.get(0);
                    //清洗余额
                    consumerBalanceService.updateConsumerBalance(consumer, cbjUtConsumer, chjUtConsumer);
                }else{
                    log.error("[监听流水更新余额失败1] message: {}", message);
                }
            }else{
                log.error("[监听流水更新余额失败2] message: {}", message);
            }
            //监听流水清洗用户余额 - end

            //处理成功
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("流水清洗余额异常,消息message:{},e:", message, e);
            //重新归入队列 or 记录失败log后续处理，丢弃消息 channel.basicAck(tag, false);
            channel.basicNack(tag, false, true);
        }
    }

}
