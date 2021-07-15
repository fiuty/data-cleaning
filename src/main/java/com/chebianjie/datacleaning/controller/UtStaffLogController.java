package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.service.UtStaffLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengdayue
 * @date: 2021-07-09
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UtStaffLogController {

    @Autowired
    private UtStaffLogService utStaffLogService;

    @GetMapping("/staffLog/cbjClean")
    public void clean() {
        utStaffLogService.firstCbjClean();
    }

    @GetMapping("/staffLog/chjClean")
    public void chjClean() {
        utStaffLogService.firstChjClean();
    }
}
