package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.common.core.response.BaseResponse;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtChargeLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.order.AutoOrder;
import com.chebianjie.datacleaning.domain.order.DushOrder;
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
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

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
//                    threadPoolExecutor.submit(new OrderTask(orderService, consumerId, phone, consumerAccount, 1));
                    orderService.cleaningCBJWashOrder(consumerId, phone, consumerAccount);
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
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
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
//                    threadPoolExecutor.submit(new OrderTask(orderService, consumerId, phone, consumerAccount, 2));
                    orderService.cleaningCHJWashOrder(consumerId, phone, consumerAccount);
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
        Map<Long, String> phoneMap = new HashMap<>();
        long startTime = DateUtil.parse(timeStr).getTime();
        List<ConsumerPhoneDTO> selfList = orderService.cbjOrderTotal(1, startTime);
        if (CollectionUtil.isNotEmpty(selfList)) {
            for (int i = 0; i < selfList.size(); i++) {
                ConsumerPhoneDTO consumerPhoneDTO = selfList.get(i);
                Long consumerId = consumerPhoneDTO.getConsumerId();
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
                            phoneMap.put(consumerId, utConsumer.getPhone());
                        }
                    }
                    if (StrUtil.isBlank(searchPhone)) {
                        log.error("出现错误手机号信息：consumerId：{}", consumerId);
                    }
                    Consumer consumer = consumerService.findByPhone(searchPhone);
                    if (consumer != null && StrUtil.isNotBlank(consumer.getUnionAccount())) {
                        String consumerAccount = consumer.getUnionAccount();
                        List<Map<String, Object>> saveAll = new ArrayList<>(utConsumpList.size());
                        for (UtConsump utConsump : utConsumpList) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", utConsump.getId());
                            map.put("consumerAccount", consumerAccount);
                            map.put("phone", searchPhone);
                            saveAll.add(map);
                        }
                        orderService.cbjUpdateOrder(1, saveAll, startTime);
                    }
                }
            }
        }

        List<ConsumerPhoneDTO> dustList = orderService.cbjOrderTotal(2, startTime);
        if (CollectionUtil.isNotEmpty(dustList)) {
            for (int i = 0; i < dustList.size(); i++) {
                ConsumerPhoneDTO consumerPhoneDTO = dustList.get(i);
                Long consumerId = consumerPhoneDTO.getConsumerId();
                String phone = consumerPhoneDTO.getPhone();
                List<DushOrder> dushOrderList = orderService.findCBJOrderByPage(2, startTime, consumerId);
                if (CollectionUtil.isNotEmpty(dushOrderList)) {
                    String searchPhone = "";
                    if (StrUtil.isNotBlank(phone)) {
                        searchPhone = phone;
                    } else {
                        String mapPhone = phoneMap.get(consumerId);
                        if (StrUtil.isNotBlank(mapPhone)) {
                            searchPhone = mapPhone;
                        } else {
                            UtConsumer utConsumer = cbjUtConsumerService.getUtConsumerById(consumerId.longValue());
                            if (utConsumer != null && StrUtil.isNotBlank(utConsumer.getPhone())) {
                                searchPhone = utConsumer.getPhone();
                                phoneMap.put(consumerId, utConsumer.getPhone());
                            }
                        }
                    }
                    if (StrUtil.isBlank(searchPhone)) {
                        log.error("出现错误手机号信息：consumerId：{}", consumerId);
                    }
                    Consumer consumer = consumerService.findByPhone(searchPhone);
                    if (consumer != null && StrUtil.isNotBlank(consumer.getUnionAccount())) {
                        String consumerAccount = consumer.getUnionAccount();
                        List<Map<String, Object>> saveAll = new ArrayList<>(dushOrderList.size());
                        for (DushOrder dushOrder : dushOrderList) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", dushOrder.getId());
                            map.put("consumerAccount", consumerAccount);
                            map.put("phone", searchPhone);
                            saveAll.add(map);
                        }
                        orderService.cbjUpdateOrder(2, saveAll, startTime);
                    }
                }
            }
        }

        List<ConsumerPhoneDTO> autoList = orderService.cbjOrderTotal(3, startTime);
        if (CollectionUtil.isNotEmpty(autoList)) {
            for (int i = 0; i < autoList.size(); i++) {
                ConsumerPhoneDTO consumerPhoneDTO = autoList.get(i);
                Long consumerId = consumerPhoneDTO.getConsumerId();
                String phone = consumerPhoneDTO.getPhone();
                List<AutoOrder> autoOrderList = orderService.findCBJOrderByPage(3, startTime, consumerId);
                if (CollectionUtil.isNotEmpty(autoOrderList)) {
                    String searchPhone = "";
                    if (StrUtil.isNotBlank(phone)) {
                        searchPhone = phone;
                    } else {
                        String mapPhone = phoneMap.get(consumerId);
                        if (StrUtil.isNotBlank(mapPhone)) {
                            searchPhone = mapPhone;
                        } else {
                            UtConsumer utConsumer = cbjUtConsumerService.getUtConsumerById(consumerId.longValue());
                            if (utConsumer != null && StrUtil.isNotBlank(utConsumer.getPhone())) {
                                searchPhone = utConsumer.getPhone();
                                phoneMap.put(consumerId, utConsumer.getPhone());
                            }
                        }
                    }
                    if (StrUtil.isBlank(searchPhone)) {
                        log.error("出现错误手机号信息：consumerId：{}", consumerId);
                    }
                    Consumer consumer = consumerService.findByPhone(searchPhone);
                    if (consumer != null && StrUtil.isNotBlank(consumer.getUnionAccount())) {
                        String consumerAccount = consumer.getUnionAccount();
                        List<Map<String, Object>> saveAll = new ArrayList<>(autoOrderList.size());
                        for (AutoOrder autoOrder : autoOrderList) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", autoOrder.getId());
                            map.put("consumerAccount", consumerAccount);
                            map.put("phone", searchPhone);
                            saveAll.add(map);
                        }
                        orderService.cbjUpdateOrder(3, saveAll, startTime);
                    }
                }
            }
        }

        List<ConsumerPhoneDTO> chargeList = orderService.cbjOrderTotal(4, startTime);
        if (CollectionUtil.isNotEmpty(chargeList)) {
            for (int i = 0; i < chargeList.size(); i++) {
                ConsumerPhoneDTO consumerPhoneDTO = chargeList.get(i);
                Long consumerId = consumerPhoneDTO.getConsumerId();
                String phone = consumerPhoneDTO.getPhone();
                List<UtChargeLog> chargeOrderList = orderService.findCBJOrderByPage(4, startTime, consumerId);
                if (CollectionUtil.isNotEmpty(chargeOrderList)) {
                    String searchPhone = "";
                    if (StrUtil.isNotBlank(phone)) {
                        searchPhone = phone;
                    } else {
                        String mapPhone = phoneMap.get(consumerId);
                        if (StrUtil.isNotBlank(mapPhone)) {
                            searchPhone = mapPhone;
                        } else {
                            UtConsumer utConsumer = cbjUtConsumerService.getUtConsumerById(consumerId.longValue());
                            if (utConsumer != null && StrUtil.isNotBlank(utConsumer.getPhone())) {
                                searchPhone = utConsumer.getPhone();
                            }
                        }
                    }
                    if (StrUtil.isBlank(searchPhone)) {
                        log.error("出现错误手机号信息：consumerId：{}", consumerId);
                    }
                    Consumer consumer = consumerService.findByPhone(searchPhone);
                    if (consumer != null && StrUtil.isNotBlank(consumer.getUnionAccount())) {
                        String consumerAccount = consumer.getUnionAccount();
                        List<Map<String, Object>> saveAll = new ArrayList<>(chargeOrderList.size());
                        for (UtChargeLog utChargeLog : chargeOrderList) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", utChargeLog.getId());
                            map.put("consumerAccount", consumerAccount);
                            map.put("phone", searchPhone);
                            saveAll.add(map);
                        }
                        orderService.cbjUpdateOrder(3, saveAll, startTime);
                    }
                }
            }
        }
        return BaseResponse.success();
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

