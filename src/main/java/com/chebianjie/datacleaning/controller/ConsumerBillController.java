package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.config.DataCleanConfiguration;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.repository.ConsumerRepository;
import com.chebianjie.datacleaning.service.ConsumerBillService;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ConsumerBillController {

    @Autowired
    private ConsumerBillService consumerBillService;

    @Autowired
    private DataCleanConfiguration dataCleanConfiguration;

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/consumerBillClean")
    public void consumerBillClean() {
        int total = consumerService.countByRegistryTimeLessThanEqual(dataCleanConfiguration.getFlowConsumerTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getFlwoConsumerStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            consumerBillService.clean(pageNumber, pageSize);
            Instant end = Instant.now();
            log.info("总页数:{},第：{}页,总用时：{}ms", totalPage, pageNumber + 1, Duration.between(now, end).toMillis());
        }
        Instant totalEnd = Instant.now();
        log.info("总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
    }

    @GetMapping("/consumerBillClean/one")
    public void consumerBillCleanOne() {
        int total = consumerService.countByRegistryTimeLessThanEqual(dataCleanConfiguration.getFlowConsumerTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getFlwoConsumerStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            consumerBillService.cleanOne(pageNumber, pageSize);
            Instant end = Instant.now();
            log.info("总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
    }

    @GetMapping("/consumerBillClean/test")
    public void test(@RequestParam("id") Long id) {
        Consumer consumer = consumerService.findById(id);
        if (consumer != null) {
            consumerBillService.threadClean(consumer);
        }
    }

    @GetMapping("/delete/fail")
    public void deleteFail() {
        consumerBillService.deleteFail();
    }


    private int computeTotalPage(long total) {
        int pageSize = 1000;
        int totalPage = (int)total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        return totalPage;
    }
}
