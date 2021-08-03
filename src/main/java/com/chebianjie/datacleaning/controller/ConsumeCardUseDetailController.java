package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.ConsumeCardUseDetail;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumeCardUseDetailService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
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
public class ConsumeCardUseDetailController {



    @Autowired
    ConsumeCardUseDetailService consumeCardUseDetailService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    AddConsumerUnionAccountLogService logService;




    @RequestMapping("/cleanCbjConsumeCardUseDetail")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjConsumeCardUseDetail(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<ConsumeCardUseDetail> consumeCardUseDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【consume_card_use_detail】 page: {} size: {}", page, size);
            consumeCardUseDetailPage = consumeCardUseDetailService.getCbjAllConsumeCardUseDetail(pageable);
            totalPage = consumeCardUseDetailPage.getTotalPages();
            List<ConsumeCardUseDetail> consumeCardUseDetailList = consumeCardUseDetailPage.getContent();
            for (int i=0;i<consumeCardUseDetailList.size();i++){
                ConsumeCardUseDetail consumeCardUseDetail = consumeCardUseDetailList.get(i);
                Long id = consumeCardUseDetail.getId();
                Long cbjId = consumeCardUseDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null?consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        consumeCardUseDetailService.updateCbjConsumeCardUseDetailById(consumerUnionAccount, id);
                        logService.saveOne(2,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(2,id,cbjId, null, null, 0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);

        return "end";
    }




    @RequestMapping("/cleanChjConsumeCardUseDetail")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjConsumeCardUseDetail(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<ConsumeCardUseDetail> consumeCardUseDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【consume_card_use_detail】page: {} size: {}", page, size);
            consumeCardUseDetailPage = consumeCardUseDetailService.getChjAllConsumeCardUseDetail(pageable);
            totalPage = consumeCardUseDetailPage.getTotalPages();
            List<ConsumeCardUseDetail> consumeCardUseDetailList = consumeCardUseDetailPage.getContent();
            for (int i=0;i<consumeCardUseDetailList.size();i++){
                ConsumeCardUseDetail consumeCardUseDetail = consumeCardUseDetailList.get(i);
                Long id = consumeCardUseDetail.getId();
                Long chjId = consumeCardUseDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        consumeCardUseDetailService.updateChjConsumeCardUseDetailById(consumerUnionAccount, id);
                        logService.saveOne(2,id,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(2,id,null, chjId, null, 0);
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
