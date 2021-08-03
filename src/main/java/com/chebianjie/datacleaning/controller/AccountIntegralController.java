package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.service.AccountIntegralService;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
@Slf4j
public class AccountIntegralController {


    @Autowired
    AccountIntegralService accountIntegralService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    AddConsumerUnionAccountLogService logService;





    @RequestMapping("/cleanCbjAccountIntegral")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjAccountIntegral(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<AccountIntegral> accountIntegralPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【account_integral】page: {} size: {}", page, size);
            accountIntegralPage = accountIntegralService.getCbjAllAccountIntegral(pageable);
            totalPage = accountIntegralPage.getTotalPages();
            List<AccountIntegral> accountIntegralList = accountIntegralPage.getContent();
            for (int i = 0; i < accountIntegralList.size(); i++) {
                AccountIntegral accountIntegral = accountIntegralList.get(i);
                Long id = accountIntegral.getId();
                Long cbjId = accountIntegral.getUid();
                List<ConsumerLog> consumerLogList = cbjId != null?consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        accountIntegralService.updateCbjAccountIntegralById(consumerUnionAccount,id);
                        //记录成功log
                        logService.saveOne(1,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        //记录无用户 失败log
                        logService.saveOne(1,id,cbjId,null,null,0);
                }

            }
            if (totalPage == 0) {
                break;
            }
            page = page + 1;
        }while (page != totalPage);

        return "end";
    }


    @RequestMapping("/cleanChjAccountIntegral")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjAccountIntegral(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<AccountIntegral> accountIntegralPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【account_integral】page: {} size: {}", page, size);
            accountIntegralPage = accountIntegralService.getChjAllAccountIntegral(pageable);
            totalPage = accountIntegralPage.getTotalPages();
            List<AccountIntegral> accountIntegralList = accountIntegralPage.getContent();
            for (int i = 0; i < accountIntegralList.size(); i++) {
                AccountIntegral accountIntegral = accountIntegralList.get(i);
                Long id = accountIntegral.getId();
                Long chjId = accountIntegral.getUid();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        accountIntegralService.updateChjAccountIntegralById(consumerUnionAccount,id);
                        //记录成功log
                        logService.saveOne(1,id,null,chjId,consumerUnionAccount,1);

                    }
                }else {
                        //记录无用户 失败log
                        logService.saveOne(1,id, null, chjId, null, 0);
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
