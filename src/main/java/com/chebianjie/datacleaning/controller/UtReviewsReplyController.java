package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtReviewsReply;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtReviewsReplyService;
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
public class UtReviewsReplyController {


    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;


    @Autowired
    UtReviewsReplyService utReviewsReplyService;

    @Autowired
    AddConsumerUnionAccountLogService logService;


    @RequestMapping("/cleanCbjUtReviewsReply")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjUtReviewsReply() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviewsReply> utReviewsReplyPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【ut_reviews_reply】page: {} size: {}", page, size);
            utReviewsReplyPage = utReviewsReplyService.getCbjAllUtReviewsReply(pageable);
            totalPage = utReviewsReplyPage.getTotalPages();
            List<UtReviewsReply> utReviewsReplyList = utReviewsReplyPage.getContent();
            for (int i = 0; i < utReviewsReplyList.size(); i++) {
                UtReviewsReply utReviewsReply = utReviewsReplyList.get(i);
                if (utReviewsReply != null) {
                    Long id = utReviewsReply.getId();
                    Long cbjId = utReviewsReply.getConsumerId();
                    List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                    if (consumerLogList.size() > 0) {
                        Long consumerId = consumerLogList.get(0).getConsumerId();
                        Consumer consumer = consumerService.findById(consumerId);
                        if (consumer != null) {
                            String consumerUnionAccount = consumer.getUnionAccount();
                            utReviewsReplyService.updateCbjUtReviewsReplyById(consumerUnionAccount, id);
                            logService.saveOne(11,cbjId,null,consumerUnionAccount,1);
                        }
                    }else{
                            logService.saveOne(11,cbjId,null,null,0);
                    }
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);

        return "end";
    }


    @RequestMapping("/cleanChjUtReviewsReply")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjUtReviewsReply() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviewsReply> utReviewsReplyPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【ut_reviews_reply】page: {} size: {}", page, size);
            utReviewsReplyPage = utReviewsReplyService.getChjAllUtReviewsReply(pageable);
            totalPage = utReviewsReplyPage.getTotalPages();
            List<UtReviewsReply> utReviewsReplyList = utReviewsReplyPage.getContent();
            for (int i = 0; i < utReviewsReplyList.size(); i++) {
                UtReviewsReply utReviewsReply = utReviewsReplyList.get(i);
                if (utReviewsReply != null) {
                    Long id = utReviewsReply.getId();
                    Long chjId = utReviewsReply.getConsumerId();
                    List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
                    if (consumerLogList.size() > 0) {
                        Long consumerId = consumerLogList.get(0).getConsumerId();
                        Consumer consumer = consumerService.findById(consumerId);
                        if (consumer != null) {
                            String consumerUnionAccount = consumer.getUnionAccount();
                            utReviewsReplyService.updateChjUtReviewsReplyById(consumerUnionAccount, id);
                            logService.saveOne(11,null,chjId,consumerUnionAccount,1);
                        }
                    }else{
                            logService.saveOne(11,null,chjId,null,0);
                    }
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);

        return "end";
    }





    @RequestMapping("/cleanCbjCouponUtReviewsReply")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjCouponUtReviewsReply() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviewsReply> utReviewsReplyPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj_coupon【ut_reviews_reply】page: {} size: {}", page, size);
            utReviewsReplyPage = utReviewsReplyService.getCbjCouponAllUtReviewsReply(pageable);
            totalPage = utReviewsReplyPage.getTotalPages();
            List<UtReviewsReply> utReviewsReplyList = utReviewsReplyPage.getContent();
            for (int i = 0; i < utReviewsReplyList.size(); i++) {
                UtReviewsReply utReviewsReply = utReviewsReplyList.get(i);
                if (utReviewsReply != null) {
                    Long id = utReviewsReply.getId();
                    Long cbjId = utReviewsReply.getConsumerId();
                    List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                    if (consumerLogList.size() > 0) {
                        Long consumerId = consumerLogList.get(0).getConsumerId();
                        Consumer consumer = consumerService.findById(consumerId);
                        if (consumer != null) {
                            String consumerUnionAccount = consumer.getUnionAccount();
                            utReviewsReplyService.updateCbjCouponUtReviewsReplyById(consumerUnionAccount, id);
                            logService.saveOne(16,cbjId,null,consumerUnionAccount,1);
                        }
                    }else{
                        logService.saveOne(16,cbjId,null,null,0);
                    }
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        } while (page != totalPage);

        return "end";
    }



    @RequestMapping("/cleanChjCouponUtReviewsReply")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjCouponUtReviewsReply() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtReviewsReply> utReviewsReplyPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj_coupon【ut_reviews_reply】page: {} size: {}", page, size);
            utReviewsReplyPage = utReviewsReplyService.getChjCouponAllUtReviewsReply(pageable);
            totalPage = utReviewsReplyPage.getTotalPages();
            List<UtReviewsReply> utReviewsReplyList = utReviewsReplyPage.getContent();
            for (int i = 0; i < utReviewsReplyList.size(); i++) {
                UtReviewsReply utReviewsReply = utReviewsReplyList.get(i);
                if (utReviewsReply != null) {
                    Long id = utReviewsReply.getId();
                    Long chjId = utReviewsReply.getConsumerId();
                    List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
                    if (consumerLogList.size() > 0) {
                        Long consumerId = consumerLogList.get(0).getConsumerId();
                        Consumer consumer = consumerService.findById(consumerId);
                        if (consumer != null) {
                            String consumerUnionAccount = consumer.getUnionAccount();
                            utReviewsReplyService.updateChjCouponUtReviewsReplyById(consumerUnionAccount, id);
                            logService.saveOne(16,null,chjId,consumerUnionAccount,1);
                        }
                    }else{
                            logService.saveOne(16,null,chjId,null,0);
                    }
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
