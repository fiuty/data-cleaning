package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.common.core.response.BaseResponse;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.order.UtConsump;
import com.chebianjie.datacleaning.dto.ConsumerPhoneDTO;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import com.chebianjie.datacleaning.service.ChjUtConsumerService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.OrderService;
import com.chebianjie.datacleaning.threads.OrderTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
    public void cleaningCBJWashOrder() throws InterruptedException {
        int totalSum = 0;


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 10, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        Long totalCount = consumerService.findTotalCount();
        int pageSize = 1000;
        int totalPage = computeTotalPage(totalCount, pageSize);
        Instant totalStartTime = Instant.now();
        for (int i = 0; i < 1; i++) {
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
                    threadPoolExecutor.submit(new OrderTask(orderService, consumerId.intValue(), phone, consumerAccount, 1));
                }
            }
            Instant endTime = Instant.now();
            log.info("总页数:{},第：{}页,总用时：{}ms", totalPage, i + 1, Duration.between(startTime, endTime).toMillis());
        }
        Instant totalEndTime = Instant.now();
        log.info("车便捷清洗洗车订单和充值订单总用时：{}ms", Duration.between(totalStartTime, totalEndTime).toMillis());
        log.info("车便捷清洗洗车订单和充值订单总次数：{}", totalSum);

    }


    /**
     * 迁移车惠捷洗车订单
     */
    @GetMapping("/cleaningCHJWashOrder")
    public void cleaningCHJWashOrder() {
        //创建线程
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 10, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
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
                    threadPoolExecutor.submit(new OrderTask(orderService, consumerId.intValue(), phone, consumerAccount, 2));
                }
            }
            Instant endTime = Instant.now();
            log.info("总页数:{},第：{}页,总用时：{}ms", totalPage, i + 1, Duration.between(startTime, endTime).toMillis());
        }
        Instant totalEndTime = Instant.now();
        log.info("车便捷清洗洗车订单和充值订单总用时：{}ms", Duration.between(totalStartTime, totalEndTime).toMillis());
    }


    @GetMapping("/cleaningTimeCBJWashOrder/{timeStr}")
    public BaseResponse cleaningTimeCBJWashOrder(@PathVariable("timeStr") String timeStr) {
        Map<Integer, String> accountMap = new HashMap<>();
        long startTime = DateUtil.parse(timeStr).getTime();
        List<ConsumerPhoneDTO> selfList = orderService.cbjOrderTotal(1, startTime);
        if (CollectionUtil.isNotEmpty(selfList)) {
            for (int i = 0; i < selfList.size(); i++) {
                ConsumerPhoneDTO consumerPhoneDTO = selfList.get(i);
                Integer consumerId = consumerPhoneDTO.getConsumerId();
                String phone = consumerPhoneDTO.getPhone();
                List<UtConsump> utConsumpList = orderService.findCBJOrderByPage(1, startTime, consumerId);
                if (CollectionUtil.isNotEmpty(utConsumpList)) {
                    String searchPhone = "";
                    if (StrUtil.isNotBlank(phone)) {
                        searchPhone = phone;
                    } else {
                        UtConsumer utConsumer = cbjUtConsumerService.getUtConsumerById(consumerId.longValue());
                        if (utConsumer != null && StrUtil.isNotBlank(utConsumer.getPhone())) {
                            searchPhone = utConsumer.getPhone();
                        }
                    }
                    if (StrUtil.isBlank(searchPhone)) {
                        log.error("出现错误手机号信息：consumerId：{}", consumerId);
                    }
                    Consumer consumer = consumerService.findByPhone(searchPhone);
                    if (consumer != null && StrUtil.isNotBlank(consumer.getUnionAccount())) {
                        String consumerAccount = consumer.getUnionAccount();
                        accountMap.put(consumerId, consumerAccount);
                        List<Object> saveAll = new ArrayList<>(utConsumpList.size());
                        for (UtConsump utConsump : utConsumpList) {
                            utConsump.setConsumerAccount(consumerAccount);
                            saveAll.add(utConsump);
                        }
                        orderService.cbjUpdateOrder(1, saveAll);
                    }
                }
            }
        }

        List<ConsumerPhoneDTO> dustList = orderService.cbjOrderTotal(2, startTime);

        List<ConsumerPhoneDTO> autoList = orderService.cbjOrderTotal(3, startTime);


        return BaseResponse.success();
    }


    public static void main(String[] args) {
        long startTime = DateUtil.parse("2021-07-29").getTime();
        System.out.println(startTime);
    }


    private int computeTotalPage(long total, int pageSize) {
        int totalPage = (int) total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        log.info("用户总记录：{}，总页数：{}", total, totalPage);
        return totalPage;
    }


}

