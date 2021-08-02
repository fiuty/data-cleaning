package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumerBalanceClearLog;
import com.chebianjie.datacleaning.domain.UtUserDriverLicens;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.UtUserDriverLicensService;
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
public class UtUserDriverLicensController {


    @Autowired
    UtUserDriverLicensService utUserDriverLicensService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    AddConsumerUnionAccountLogService logService;


    @RequestMapping("/cleanCbjUtUtUserDriverLicens")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjUtUtUserDriverLicens() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtUserDriverLicens> utUtUserDriverLicensPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("page: {} size: {}", page, size);
            utUtUserDriverLicensPage = utUserDriverLicensService.getCbjAllUtUserDriverLicens(pageable);
            totalPage = utUtUserDriverLicensPage.getTotalPages();
            List<UtUserDriverLicens> utUserDriverLicensList = utUtUserDriverLicensPage.getContent();
            for (int i = 0; i < utUserDriverLicensList.size(); i++) {
                UtUserDriverLicens utUserDriverLicens = utUserDriverLicensList.get(i);
                Long id = utUserDriverLicens.getId() ;
                Long cbjId = utUserDriverLicens.getUid();
                List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if (consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utUserDriverLicensService.updateCbjUtUserDriverLicensById(consumerUnionAccount,id);
                        logService.saveOne(13,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(13,cbjId,null,null,0);
                }

                if (totalPage == 0) {
                    break;
                }
                page = page + 1;
            }

        }while (page != totalPage) ;
        return "end";

    }


    @RequestMapping("/cleanChjUtUtUserDriverLicens")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjUtUtUserDriverLicens() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<UtUserDriverLicens> utUtUserDriverLicensPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("page: {} size: {}", page, size);
            utUtUserDriverLicensPage = utUserDriverLicensService.getChjAllUtUserDriverLicens(pageable);
            totalPage = utUtUserDriverLicensPage.getTotalPages();
            List<UtUserDriverLicens> utUserDriverLicensList = utUtUserDriverLicensPage.getContent();
            for (int i = 0; i < utUserDriverLicensList.size(); i++) {
                UtUserDriverLicens utUserDriverLicens = utUserDriverLicensList.get(i);
                Long id =  utUserDriverLicens.getId();
                Long chjId = utUserDriverLicens.getUid() ;
                List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        utUserDriverLicensService.updateChjUtUserDriverLicensById(consumerUnionAccount,id);
                        logService.saveOne(13,null,chjId,consumerUnionAccount,1);
                    }
                }else{
                        logService.saveOne(13,null,chjId,null,0);
                }

                if (totalPage == 0) {
                    break;
                }
                page = page + 1;
            }

        }while (page != totalPage) ;
        return "end";

    }








}
