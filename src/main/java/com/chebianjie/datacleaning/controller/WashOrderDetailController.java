package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumerLog;
import com.chebianjie.datacleaning.domain.WashOrderDetail;
import com.chebianjie.datacleaning.service.*;
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
public class WashOrderDetailController {


    @Autowired
    WashOrderDetailService washOrderDetailService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    AddConsumerUnionAccountLogService logService;

    @RequestMapping("/cleanCbjWashOrderDetail")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjWashOrderDetail(){

        int totalPage;
        int page = 0;
        int size = 10;
        Page<WashOrderDetail> washOrderDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【wash_order_detail】page: {} size: {}", page, size);
            washOrderDetailPage = washOrderDetailService.getCbjAllWashOrderDetail(pageable);
            totalPage = washOrderDetailPage.getTotalPages();
            List<WashOrderDetail> utConsumerLogList = washOrderDetailPage.getContent();
            for (int i = 0; i < utConsumerLogList.size(); i++) {
                WashOrderDetail washOrderDetail = utConsumerLogList.get(i);
                Long id = washOrderDetail.getId();
                Long cbjId = washOrderDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null ? consumerLogService.getCbjConsumerLogByConsumerId(cbjId) : new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        washOrderDetailService.updateCbjWashOrderDetail(consumerUnionAccount,id);
                        logService.saveOne(14,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(14,id,cbjId,null,null,0);
                }

            }
            if (totalPage == 0) {
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }




    @RequestMapping("/cleanChjWashOrderDetail")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjWashOrderDetail(){

        int totalPage;
        int page = 0;
        int size = 10;
        Page<WashOrderDetail> washOrderDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【wash_order_detail】page: {} size: {}", page, size);
            washOrderDetailPage = washOrderDetailService.getChjAllWashOrderDetail(pageable);
            totalPage = washOrderDetailPage.getTotalPages();
            List<WashOrderDetail> utConsumerLogList = washOrderDetailPage.getContent();
            for (int i = 0; i < utConsumerLogList.size(); i++) {
                WashOrderDetail washOrderDetail = utConsumerLogList.get(i);
                Long id = washOrderDetail.getId();
                Long chjId = washOrderDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId != null ? consumerLogService.getChjConsumerLogByConsumerId(chjId) :  new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        washOrderDetailService.updateChjWashOrderDetail(consumerUnionAccount,id);
                        logService.saveOne(14,id,null,chjId,consumerUnionAccount,1);
                    }

                }else {
                        logService.saveOne(14,id,null,chjId,null,0);
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
