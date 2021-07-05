package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.service.ConsumerBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@RequestMapping("/api")
public class JobController {

    @Autowired
    private ConsumerBillService consumerBillService;

    @GetMapping("/consumerBill/job")
    public void consumerBillJob() {
        consumerBillService.consumerBillJob();
    }
}
