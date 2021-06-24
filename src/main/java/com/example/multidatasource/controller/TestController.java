package com.example.multidatasource.controller;

import com.example.multidatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Object test(){
        return "hello ~";
    }

    @GetMapping("/master")
    public Object testMaster(){
        return userService.listMaster();
    }

    @GetMapping("/slave")
    public Object testSlave(){
        return userService.listSlave();
    }
}
