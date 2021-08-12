package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.config.DataCleanConfiguration;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.dto.AddBillLogMessage;
import com.chebianjie.datacleaning.service.AddBillLogService;
import com.chebianjie.datacleaning.service.ConsumerBillService;
import com.chebianjie.datacleaning.service.FailConsumerBillLogService;
import com.chebianjie.datacleaning.util.CleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
@RestController
@RequestMapping("/api/fail")
@Slf4j
public class FailController {

    @Autowired
    private AddBillLogService addBillLogService;

    @Autowired
    private DataCleanConfiguration dataCleanConfiguration;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConsumerBillService consumerBillService;

    @GetMapping("/addBillLog")
    public void addBillLog() {
        int total = addBillLogService.countByIdLessThanEqual(46900L);
        int totalPage = CleanUtil.computeTotalPage(total);
        int pageSize = 1000;
        log.info("failController--addBillLog total:{},totalPage:{}", total, totalPage);
        for (int i = dataCleanConfiguration.getFailAddBillLogPage(); i < totalPage; i++) {
            Instant now = Instant.now();
            List<AddBillLog> addBillLogs = addBillLogService.findAllByPage(i, pageSize);
            addBillLogs.forEach(addBillLog -> {
                AddBillLogMessage addBillLogMessage = new AddBillLogMessage();
                addBillLogMessage.setUtUserTotalFlowId(addBillLog.getUtUserTotalFlowId());
                addBillLogMessage.setPlatform(addBillLog.getPlatform());
                rabbitTemplate.convertAndSend(RabbitMqConstants.ADD_BILL_LOG_EXCHANGE, RabbitMqConstants.ADD_BILL_LOG_ROUTING_KEY, addBillLogMessage);
            });
            Instant end = Instant.now();
            log.info("failController--addBillLog 当前页：{}，总页数：{}，用时：{}s", i, totalPage, Duration.between(now, end).toMillis() / 1000);
        }
    }

    @GetMapping("/handleFail")
    public void handleFail() {
        consumerBillService.handleFailDetail();
    }

    @GetMapping("/handleFail/one")
    public void handleFailOne(Long id) {
        consumerBillService.handleFailOne(id);
    }

    @GetMapping("/fix/oldUtUserTotalFlow")
    public void fixOldUtUserTotalFlow(@RequestParam("phone") String phone) {
        consumerBillService.fixOldUtUserTotalFlow(phone);
    }

}