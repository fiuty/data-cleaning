package com.chebianjie.datacleaning.config;


import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * rabbitmq配置类
 */
@Configuration
@Slf4j
public class RabbitmqConfig {

    @Autowired
    private Environment env;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     *
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    /**
     *多个消费者实例的配置
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        //设置消息在传输中的格式。在这里采用JSON的格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式为自动确认机制
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置并发消费者实例的初始数量。
        factory.setConcurrentConsumers(10);
        //设置并发消费者实例的最大数量。
        factory.setMaxConcurrentConsumers(20);
        //设置并发消费者实例中每个实例拉取的消息数量。
        factory.setPrefetchCount(10);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        //rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;

    }

    /**
     * 确认消费模式为手动确认机制-MANUAL
     */
    @Bean
    public SimpleRabbitListenerContainerFactory singleListenerContainerManual(){
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        //设置消息在传输中的格式，在这里采用JSON的格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置并发消费者实例的初始数量。在这里为1个
        factory.setConcurrentConsumers(1);
        //设置并发消费者实例的最大数量。在这里为1个
        factory.setMaxConcurrentConsumers(1);
        //设置并发消费者实例中每个实例拉取的消息数量-在这里为1个
        factory.setPrefetchCount(1);
        //确认消费模式为自动确认机制
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    //队列
    @Bean
    public Queue billQueue() {
        return new Queue(RabbitMqConstants.DATA_CLEAN_BILL_QUEUE, true);
    }

    //交换机
    @Bean
    public DirectExchange billExchange() {
        return new DirectExchange(RabbitMqConstants.DATA_CLEAN_BILL_EXCHANGE, true, false);
    }

    //路由绑定
    @Bean
    public Binding billBinding() {
        return BindingBuilder.bind(billQueue()).to(billExchange()).with(RabbitMqConstants.DATA_CLEAN_BILL_ROUTING_KEY);
    }

    //流水队列
    @Bean
    public Queue firstBillQueue() {
        return new Queue(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_QUEUE, true);
    }

    //流水交换机
    @Bean
    public DirectExchange firstBillExchange() {
        return new DirectExchange(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_EXCHANGE, true, false);
    }
    //流水绑定关系
    @Bean
    public Binding firstBillBinding() {
        return BindingBuilder.bind(firstBillQueue()).to(firstBillExchange()).with(RabbitMqConstants.DATA_CLEAN_FIRST_BILL_ROUTING_KEY);
    }

    //员工业绩
    @Bean
    public Queue firstStaffLogQueue() {
        return new Queue(RabbitMqConstants.DATA_CLEAN_FIRST_STAFF_LOG_QUEUE, true);
    }

    @Bean
    public DirectExchange firstStaffLogExchange() {
        return new DirectExchange(RabbitMqConstants.DATA_CLEAN_FIRST_STAFF_LOG_EXCHANGE, true, false);
    }

    @Bean
    public Binding firstStaffLogBinding() {
        return BindingBuilder.bind(firstStaffLogQueue()).to(firstStaffLogExchange()).with(RabbitMqConstants.DATA_CLEAN_FIRST_STAFF_LOG_ROUTING_KEY);
    }
}

