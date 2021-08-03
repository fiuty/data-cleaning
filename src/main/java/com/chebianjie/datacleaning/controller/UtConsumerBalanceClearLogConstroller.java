package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.ConsumeCardUseDetail;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumerBalanceClearLog;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtConsumerBalanceClearLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UtConsumerBalanceClearLogConstroller {

    @Autowired
    UtConsumerBalanceClearLogService utConsumerBalanceClearLogService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    AddConsumerUnionAccountLogService logService;


    @RequestMapping("/cleanCbjUtConsumerBalanceClearLog")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjUtConsumerBalanceClearLog() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtConsumerBalanceClearLog> consumerBalanceClearLogPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_consumer_balance_clear_log】page: {} size: {}", page, size);
            consumerBalanceClearLogPage = utConsumerBalanceClearLogService.getCbjAllUtConsumerBalanceClearLog(pageable);
            totalPage = consumerBalanceClearLogPage.getTotalPages();
            List<UtConsumerBalanceClearLog> consumerBalanceClearLogList = consumerBalanceClearLogPage.getContent();
            for (int i = 0; i < consumerBalanceClearLogList.size(); i++) {
                UtConsumerBalanceClearLog utConsumerBalanceClearLog = consumerBalanceClearLogList.get(i);
                Long id = utConsumerBalanceClearLog.getId();
                Long cbjId = utConsumerBalanceClearLog.getUid();
                List<ConsumerLog> consumerLogList = cbjId != null?consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utConsumerBalanceClearLogService.updateCbjUtConsumerBalanceClearLogById(consumerUnionAccount,id);
                        logService.saveOne(6,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                    logService.saveOne(6, id, cbjId, null, null, 0);
                }

                if (totalPage == 0) {
                    break;
                }
                page = page + 1;
            }

        }while (page != totalPage) ;
            return "end";

    }





    @RequestMapping("/cleanChjUtConsumerBalanceClearLog")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjUtConsumerBalanceClearLog() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtConsumerBalanceClearLog> consumerBalanceClearLogPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_consumer_balance_clear_log】page: {} size: {}", page, size);
            consumerBalanceClearLogPage = utConsumerBalanceClearLogService.getChjAllUtConsumerBalanceClearLog(pageable);
            totalPage = consumerBalanceClearLogPage.getTotalPages();
            List<UtConsumerBalanceClearLog> consumerBalanceClearLogList = consumerBalanceClearLogPage.getContent();
            for (int i = 0; i < consumerBalanceClearLogList.size(); i++) {
                UtConsumerBalanceClearLog utConsumerBalanceClearLog = consumerBalanceClearLogList.get(i);
                Long id = utConsumerBalanceClearLog.getId();
                Long chjId = utConsumerBalanceClearLog.getUid();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utConsumerBalanceClearLogService.updateChjUtConsumerBalanceClearLogById(consumerUnionAccount,id);
                        logService.saveOne(6,id,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(6, id,null, chjId, null, 0);
                }
            }
            if (totalPage == 0) {
                break;
            }
            page = page + 1;
        }while (page != totalPage) ;
        return "end";

    }



}
