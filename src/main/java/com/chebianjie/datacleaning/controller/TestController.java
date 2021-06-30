package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("test")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test() {
        consumerService.listMster();
        consumerService.listSlave();
    }

}
