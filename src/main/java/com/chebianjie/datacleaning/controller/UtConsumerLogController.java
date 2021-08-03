package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.AccountIntegral;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumerLog;
import com.chebianjie.datacleaning.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UtConsumerLogController {


    @Autowired
    UtConsumerLogService utConsumerLogService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    AddConsumerUnionAccountLogService logService;

    @RequestMapping("/cleanCbjUtConsumerLog")
    public String cleanCbjUtConsumerLog(){

        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtConsumerLog> utConsumerLogPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_consumer_log】page: {} size: {}", page, size);
            utConsumerLogPage = utConsumerLogService.getCbjAllUtConsumerLog(pageable);
            totalPage = utConsumerLogPage.getTotalPages();
            List<UtConsumerLog> utConsumerLogList = utConsumerLogPage.getContent();
            for (int i = 0; i < utConsumerLogList.size(); i++) {
                UtConsumerLog utConsumerLog = utConsumerLogList.get(i);
                Long id = utConsumerLog.getId();
                Long cbjId = utConsumerLog.getUid();
                List<ConsumerLog> consumerLogList = cbjId != null?consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utConsumerLogService.updateCbjUtConsumerLog(consumerUnionAccount,id);
                        logService.saveOne(7,id,cbjId,null,consumerUnionAccount,1);
                    }

                }else {
                        logService.saveOne(7,id,cbjId, null, null, 0);
                }
            }
            if (totalPage == 0) {
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }


    @RequestMapping("/cleanChjUtConsumerLog")
    public String cleanChjUtConsumerLog(){

        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtConsumerLog> utConsumerLogPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_consumer_log】page: {} size: {}", page, size);
            utConsumerLogPage = utConsumerLogService.getChjAllUtConsumerLog(pageable);
            totalPage = utConsumerLogPage.getTotalPages();
            List<UtConsumerLog> utConsumerLogList = utConsumerLogPage.getContent();
            for (int i = 0; i < utConsumerLogList.size(); i++) {
                UtConsumerLog utConsumerLog = utConsumerLogList.get(i);
                Long id = utConsumerLog.getId();
                Long chjId = utConsumerLog.getUid();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utConsumerLogService.updateChjUtConsumerLog(consumerUnionAccount,id);
                        logService.saveOne(7,id,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(7,id,null,chjId,null,0);
                }
            }
            if (totalPage == 0) {
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }



}
