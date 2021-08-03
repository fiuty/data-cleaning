package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtSiteRecommend;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtSiteRecommendService;
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
public class UtSiteRecommendController {
    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    UtSiteRecommendService utSiteRecommendService;

    @Autowired
    AddConsumerUnionAccountLogService logService;

    @RequestMapping("/cleanCbjUtSiteRecommend")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String  cleanCbjUtSiteRecommend(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtSiteRecommend> utSiteRecommendPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_site_recommend】page: {} size: {}", page, size);
            utSiteRecommendPage = utSiteRecommendService.getCbjAllUtSiteRecommend(pageable);
            totalPage = utSiteRecommendPage.getTotalPages();
            List<UtSiteRecommend> utSiteRecommendList = utSiteRecommendPage.getContent();
            for(int i=0;i < utSiteRecommendList.size();i++){
                UtSiteRecommend utSiteRecommend = utSiteRecommendList.get(i);
                Long id = utSiteRecommend.getId();
                Long cbjId = utSiteRecommend.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null ?consumerLogService.getCbjConsumerLogByConsumerId(cbjId) : new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utSiteRecommendService.updateCbjUtSiteRecommendById(consumerUnionAccount, id);
                        logService.saveOne(12,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(12,id,cbjId,null,null,0);
                }

            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";

    }


    @RequestMapping("/cleanChjUtSiteRecommend")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String  cleanChjUtSiteRecommend(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtSiteRecommend> utSiteRecommendPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_site_recommend】page: {} size: {}", page, size);
            utSiteRecommendPage = utSiteRecommendService.getChjAllUtSiteRecommend(pageable);
            totalPage = utSiteRecommendPage.getTotalPages();
            List<UtSiteRecommend> utSiteRecommendList = utSiteRecommendPage.getContent();
            for(int i=0;i < utSiteRecommendList.size();i++){
                UtSiteRecommend utSiteRecommend = utSiteRecommendList.get(i);
                Long id = utSiteRecommend.getId();
                Long chjId = utSiteRecommend.getConsumerId();
                List<ConsumerLog> consumerLogList =chjId !=null ? consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utSiteRecommendService.updateChjUtSiteRecommendById(consumerUnionAccount, id);
                        logService.saveOne(12,id,null,chjId,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(12,id,null,chjId,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page!= totalPage);
        return "end";


    }







    @RequestMapping("/cleanCbjCouponUtSiteRecommend")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String  cleanCbjCouponUtSiteRecommend(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtSiteRecommend> utSiteRecommendPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj_coupon【ut_site_recommend】page: {} size: {}", page, size);
            utSiteRecommendPage = utSiteRecommendService.getCbjCouponAllUtSiteRecommend(pageable);
            totalPage = utSiteRecommendPage.getTotalPages();
            List<UtSiteRecommend> utSiteRecommendList = utSiteRecommendPage.getContent();
            for(int i=0;i < utSiteRecommendList.size();i++){
                UtSiteRecommend utSiteRecommend = utSiteRecommendList.get(i);
                Long id = utSiteRecommend.getId();
                Long cbjId = utSiteRecommend.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null ? consumerLogService.getCbjConsumerLogByConsumerId(cbjId) : new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utSiteRecommendService.updateCbjCouponUtSiteRecommendById(consumerUnionAccount, id);
                        logService.saveOne(18,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(18,id,cbjId,null,null,0);
                }

            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";

    }



    @RequestMapping("/cleanChjCouponUtSiteRecommend")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String  cleanChjCouponUtSiteRecommend(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtSiteRecommend> utSiteRecommendPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj_coupon【ut_site_recommend】page: {} size: {}", page, size);
            utSiteRecommendPage = utSiteRecommendService.getChjCouponAllUtSiteRecommend(pageable);
            totalPage = utSiteRecommendPage.getTotalPages();
            List<UtSiteRecommend> utSiteRecommendList = utSiteRecommendPage.getContent();
            for(int i=0;i < utSiteRecommendList.size();i++){
                UtSiteRecommend utSiteRecommend = utSiteRecommendList.get(i);
                Long id = utSiteRecommend.getId();
                Long chjId = utSiteRecommend.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId != null ? consumerLogService.getChjConsumerLogByConsumerId(chjId) : new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utSiteRecommendService.updateChjCouponUtSiteRecommendById(consumerUnionAccount, id);
                        logService.saveOne(18,id,null,chjId,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(18,id,null,chjId,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page!= totalPage);
        return "end";


    }




}
