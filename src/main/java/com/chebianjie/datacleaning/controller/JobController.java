package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.service.ConsumerBillService;
import com.chebianjie.datacleaning.service.UtStaffLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@RequestMapping("/api")
@RestController
public class JobController {

    @Autowired
    private ConsumerBillService consumerBillService;

    @Autowired
    private UtStaffLogService utStaffLogService;

    @GetMapping("/consumerBill/job")
    public void consumerBillJob() {
        consumerBillService.consumerBillJob();
    }

    @GetMapping("/utstaffLog/job")
    public void utstaffLogJob() {
        utStaffLogService.utstaffLogJob();
    }
}
