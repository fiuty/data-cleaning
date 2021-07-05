package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.ConsumerLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class ConsumerLogController extends AbstractBaseController{

    @GetMapping
    public Object test(){
        consumerLogService.saveOne(new ConsumerLog());
        return "OK";
    }
}
