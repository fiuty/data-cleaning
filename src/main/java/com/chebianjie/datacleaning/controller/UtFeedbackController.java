package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtFeedback;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtFeedbackService;
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
public class UtFeedbackController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    UtFeedbackService utFeedbackService;

    @Autowired
    AddConsumerUnionAccountLogService logService;


    @RequestMapping("/cleanCbjUtFeedback")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjUtFeedback() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtFeedback> utFeedbackPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_feedback】page: {} size: {}", page, size);
            utFeedbackPage = utFeedbackService.getCbjAllUtFeedback(pageable);
            totalPage = utFeedbackPage.getTotalPages();
            List<UtFeedback> utFeedbackList = utFeedbackPage.getContent();
            for(int i=0;i < utFeedbackList.size();i++){
                UtFeedback utFeedback = utFeedbackList.get(i);
                Long id = utFeedback.getId();
                Long cbjId = utFeedback.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null ? consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utFeedbackService.updateCbjUtFeedbackById(consumerUnionAccount, id);
                        logService.saveOne(9,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(9,id,cbjId,null,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }


    @RequestMapping("/cleanChjUtFeedback")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjUtFeedback() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtFeedback> utFeedbackPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_feedback】page: {} size: {}", page, size);
            utFeedbackPage = utFeedbackService.getChjAllUtFeedback(pageable);
            totalPage = utFeedbackPage.getTotalPages();
            List<UtFeedback> utFeedbackList = utFeedbackPage.getContent();
            for(int i=0;i < utFeedbackList.size();i++){
                UtFeedback utFeedback = utFeedbackList.get(i);
                Long id = utFeedback.getId();
                Long chjId = utFeedback.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utFeedbackService.updateChjUtFeedbackById(consumerUnionAccount, id);
                        logService.saveOne(9,id,null,chjId,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(9,id,null,chjId,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }







    @RequestMapping("/cleanCbjCouponUtFeedback")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjCouponUtFeedback() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtFeedback> utFeedbackPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj Coupon【ut_feedback】page: {} size: {}", page, size);
            utFeedbackPage = utFeedbackService.getCbjCouponAllUtFeedback(pageable);
            totalPage = utFeedbackPage.getTotalPages();
            List<UtFeedback> utFeedbackList = utFeedbackPage.getContent();
            for(int i=0;i < utFeedbackList.size();i++){
                UtFeedback utFeedback = utFeedbackList.get(i);
                Long id = utFeedback.getId();
                Long cbjId = utFeedback.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId!=null ? consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utFeedbackService.updateCbjCouponUtFeedbackById(consumerUnionAccount, id);
                        logService.saveOne(17,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(17,id,cbjId,null,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }



    @RequestMapping("/cleanChjCouponUtFeedback")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjCouponUtFeedback() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtFeedback> utFeedbackPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj Coupon【ut_feedback】page: {} size: {}", page, size);
            utFeedbackPage = utFeedbackService.getChjCouponAllUtFeedback(pageable);
            totalPage = utFeedbackPage.getTotalPages();
            List<UtFeedback> utFeedbackList = utFeedbackPage.getContent();
            for(int i=0;i < utFeedbackList.size();i++){
                UtFeedback utFeedback = utFeedbackList.get(i);
                Long id = utFeedback.getId();
                Long chjId = utFeedback.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId !=null? consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utFeedbackService.updateChjCouponUtFeedbackById(consumerUnionAccount, id);
                        logService.saveOne(17,id,null,chjId,consumerUnionAccount,1);
                    }
                }else{
                    logService.saveOne(17,id,null,chjId,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }




}
