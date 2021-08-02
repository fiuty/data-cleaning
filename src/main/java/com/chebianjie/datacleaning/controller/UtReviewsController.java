package com.chebianjie.datacleaning.controller;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.*;
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

import java.util.List;

@RestController
@Slf4j
public class UtReviewsController {


    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    UtReviewsService utReviewsService;

    @Autowired
    AddConsumerUnionAccountLogService logService;


    @RequestMapping("/cleanCbjUtReviews")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjUtReviews() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviews> utReviewsPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_reviews】page: {} size: {}", page, size);
            utReviewsPage = utReviewsService.getCbjAllUtReviews(pageable);
            totalPage = utReviewsPage.getTotalPages();
            List<UtReviews> utReviewsList = utReviewsPage.getContent();
            for (int i = 0; i < utReviewsList.size(); i++) {
                UtReviews utReviews = utReviewsList.get(i);
                Long id = utReviews.getId();
                Long cbjId = utReviews.getConsumerId();
                //根据车便捷consumerId查用日志表
                List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                if (consumerLogList.size() > 0) {
                    //原consumerId
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if (consumer != null) {
                        //用户唯一标识
                        String consumerUnionAccount = consumer.getUnionAccount();
                        //执行更改
                        utReviewsService.updateCbjUtReviewsById(consumerUnionAccount, id);
                        logService.saveOne(10,cbjId,null,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(10,cbjId,null,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);
        return "end";
    }


    @RequestMapping("/cleanChjUtReviews")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjUtReviews() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviews> utReviewsPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_reviews】page: {} size: {}", page, size);
            utReviewsPage = utReviewsService.getChjAllUtReviews(pageable);
            totalPage = utReviewsPage.getTotalPages();
            List<UtReviews> utReviewsList = utReviewsPage.getContent();
            for (int i = 0; i < utReviewsList.size(); i++) {
                UtReviews utReviews = utReviewsList.get(i);
                Long id = utReviews.getId();
                Long chjId = utReviews.getConsumerId();
                List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
                if (consumerLogList.size() > 0) {
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if (consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utReviewsService.updateChjUtReviewsById(consumerUnionAccount, id);
                        logService.saveOne(10,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(10,null,chjId,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);
        return "end";
    }












    @RequestMapping("/cleanCbjCouponUtReviews")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjCouponUtReviews() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviews> utReviewsPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj_coupon【ut_reviews】page: {} size: {}", page, size);
            utReviewsPage = utReviewsService.getCbjCouponAllUtReviews(pageable);
            totalPage = utReviewsPage.getTotalPages();
            List<UtReviews> utReviewsList = utReviewsPage.getContent();
            for (int i = 0; i < utReviewsList.size(); i++) {
                UtReviews utReviews = utReviewsList.get(i);
                Long id = utReviews.getId();
                Long cbjId = utReviews.getConsumerId();
                //根据车便捷consumerId查用日志表
                List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                if (consumerLogList.size() > 0) {
                    //原consumerId
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if (consumer != null) {
                        //用户唯一标识
                        String consumerUnionAccount = consumer.getUnionAccount();
                        //执行更改
                        utReviewsService.updateCbjCouponUtReviewsById(consumerUnionAccount, id);
                        logService.saveOne(15,cbjId,null,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(15,cbjId,null,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);
        return "end";
    }




    @RequestMapping("/cleanChjCouponUtReviews")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjCouponUtReviews() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviews> utReviewsPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj_coupon【ut_reviews】page: {} size: {}", page, size);
            utReviewsPage = utReviewsService.getChjAllUtReviews(pageable);
            totalPage = utReviewsPage.getTotalPages();
            List<UtReviews> utReviewsList = utReviewsPage.getContent();
            for (int i = 0; i < utReviewsList.size(); i++) {
                UtReviews utReviews = utReviewsList.get(i);
                Long id = utReviews.getId();
                Long chjId = utReviews.getConsumerId();
                List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
                if (consumerLogList.size() > 0) {
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if (consumer != null) {
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utReviewsService.updateChjCouponUtReviewsById(consumerUnionAccount, id);
                        logService.saveOne(15,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(15,null,chjId,null,0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);
        return "end";
    }








}
