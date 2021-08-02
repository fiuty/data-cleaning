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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
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
    public String cleanCbjWashOrderDetail(){

        int totalPage;
        int page = 0;
        int size = 10;
        Page<WashOrderDetail> washOrderDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("page: {} size: {}", page, size);
            washOrderDetailPage = washOrderDetailService.getCbjAllWashOrderDetail(pageable);
            totalPage = washOrderDetailPage.getTotalPages();
            List<WashOrderDetail> utConsumerLogList = washOrderDetailPage.getContent();
            for (int i = 0; i < utConsumerLogList.size(); i++) {
                WashOrderDetail washOrderDetail = utConsumerLogList.get(i);
                Long id = washOrderDetail.getId();
                Long cbjId = washOrderDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        washOrderDetailService.updateCbjWashOrderDetail(consumerUnionAccount,id);
                        logService.saveOne(14,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(14,cbjId,null,null,0);
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
    public String cleanChjWashOrderDetail(){

        int totalPage;
        int page = 0;
        int size = 10;
        Page<WashOrderDetail> washOrderDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("page: {} size: {}", page, size);
            washOrderDetailPage = washOrderDetailService.getChjAllWashOrderDetail(pageable);
            totalPage = washOrderDetailPage.getTotalPages();
            List<WashOrderDetail> utConsumerLogList = washOrderDetailPage.getContent();
            for (int i = 0; i < utConsumerLogList.size(); i++) {
                WashOrderDetail washOrderDetail = utConsumerLogList.get(i);
                Long id = washOrderDetail.getId();
                Long cbjId = washOrderDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(cbjId);
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        washOrderDetailService.updateChjWashOrderDetail(consumerUnionAccount,id);
                        logService.saveOne(14,cbjId,null,consumerUnionAccount,1);
                    }

                }else {
                        logService.saveOne(14,cbjId,null,null,0);
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
