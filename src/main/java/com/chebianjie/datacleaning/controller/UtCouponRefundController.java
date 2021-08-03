package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtCouponRefund;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtCouponRefundService;
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
public class UtCouponRefundController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    UtCouponRefundService utCouponRefundService;

    @Autowired
    AddConsumerUnionAccountLogService logService;



    @RequestMapping("/cleanCbjUtCouponRefund")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjUtCouponRefund() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtCouponRefund> utCouponRefundPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_coupon_refund】page: {} size: {}", page, size);
            utCouponRefundPage = utCouponRefundService.getCbjAllUtCouponRefund(pageable);
            totalPage = utCouponRefundPage.getTotalPages();
            List<UtCouponRefund> utCouponRefundList = utCouponRefundPage.getContent();
            for(int i=0;i < utCouponRefundList.size();i++){
                UtCouponRefund utCouponRefund = utCouponRefundList.get(i);
                Long id = utCouponRefund.getId();
                Long cbjId = utCouponRefund.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null ? consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utCouponRefundService.updateCbjUtCouponRefundById(consumerUnionAccount, id);
                        logService.saveOne(8,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(8,id,cbjId,null,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }


    @RequestMapping("/cleanChjUtCouponRefund")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjUtCouponRefund() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtCouponRefund> utCouponRefundPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_coupon_refund】page: {} size: {}", page, size);
            utCouponRefundPage = utCouponRefundService.getChjAllUtCouponRefund(pageable);
            totalPage = utCouponRefundPage.getTotalPages();
            List<UtCouponRefund> utCouponRefundList = utCouponRefundPage.getContent();
            for(int i=0;i < utCouponRefundList.size();i++){
                UtCouponRefund utCouponRefund = utCouponRefundList.get(i);
                Long id = utCouponRefund.getId();
                Long chjId = utCouponRefund.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utCouponRefundService.updateChjUtCouponRefundById(consumerUnionAccount, id);
                        logService.saveOne(8,id,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(8,id,null,chjId,null,0);
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
