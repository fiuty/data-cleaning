package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import com.chebianjie.datacleaning.service.ChjUtConsumerService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.OrderService;
import com.chebianjie.datacleaning.threads.OrderTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2021-06-28
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrderController {

    @Autowired
    private CbjUtConsumerService cbjUtConsumerService;

    @Autowired
    private ChjUtConsumerService chjUtConsumerService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private OrderService orderService;

    /**
     * 迁移车便捷洗车订单
     */
    @GetMapping("/cleaningCBJWashOrder")
    public void cleaningCBJWashOrder() {
        int totalSum = 0;
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(100);

        Long totalCount = consumerService.findTotalCount();
        int pageSize = 1000;
        int totalPage = computeTotalPage(totalCount, pageSize);
        Instant totalStartTime = Instant.now();
        for (int i = 0; i < totalPage; i++) {
            totalSum++;
            List<Consumer> consumerList = consumerService.findAllByPage(i * pageSize, pageSize);
            Instant startTime = Instant.now();
            for (Consumer consumer : consumerList) {
                String phone = consumer.getPhone();
                String consumerAccount = consumer.getUnionAccount();
                UtConsumer cbjConsumer = cbjUtConsumerService.getUtConsumerByPhone(phone);
                if (cbjConsumer != null && consumer != null) {
                    Long consumerId = cbjConsumer.getId();
                    log.info("清洗车便捷用户洗车订单和充值订单----用户id：{}，手机号：{}，唯一标识：{}=======", consumerId, phone, consumerAccount);
                    es.submit(new OrderTask(orderService, consumerId, phone, consumerAccount, 1));
                }
            }
            Instant endTime = Instant.now();
            log.info("总页数:{},第：{}页,总用时：{}ms", totalPage, i + 1, Duration.between(startTime, endTime).toMillis());
        }
        Instant totalEndTime = Instant.now();
        log.info("车便捷清洗洗车订单和充值订单总用时：{}ms",  Duration.between(totalStartTime, totalEndTime).toMillis());


        log.info("车便捷清洗洗车订单和充值订单总次数：{}",  totalSum);


    }




    /**
     * 迁移车惠捷洗车订单
     */
    @GetMapping("/cleaningCHJWashOrder")
    public void cleaningCHJWashOrder() {
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(100);

        Long totalCount = consumerService.findTotalCount();
        int pageSize = 1000;
        int totalPage = computeTotalPage(totalCount, pageSize);
        Instant totalStartTime = Instant.now();
        for (int i = 0; i < totalPage; i++) {
            List<Consumer> consumerList = consumerService.findAllByPage(i * pageSize, pageSize);
            Instant startTime = Instant.now();
            for (Consumer consumer : consumerList) {
                String phone = consumer.getPhone();
                String consumerAccount = consumer.getUnionAccount();
                UtConsumer chjConsumer = chjUtConsumerService.getUtConsumerByPhone(phone);
                if (chjConsumer != null && consumer != null) {
                    Long consumerId = chjConsumer.getId();
                    log.info("清洗车便捷用户洗车订单和充值订单----用户id：{}，手机号：{}，唯一标识：{}=======", consumerId, phone, consumerAccount);
//                    orderService.cleaningCBJWashOrder(consumerId, consumerAccount);
                    es.submit(new OrderTask(orderService, consumerId, phone, consumerAccount, 2));
                }
            }
            Instant endTime = Instant.now();
            log.info("总页数:{},第：{}页,总用时：{}ms", totalPage, i + 1, Duration.between(startTime, endTime).toMillis());
        }
        Instant totalEndTime = Instant.now();
        log.info("车便捷清洗洗车订单和充值订单总用时：{}ms",  Duration.between(totalStartTime, totalEndTime).toMillis());
    }


    private int computeTotalPage(long total, int pageSize) {
        int totalPage = (int)total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        log.info("用户总记录：{}，总页数：{}", total, totalPage);
        return totalPage;
    }


}

