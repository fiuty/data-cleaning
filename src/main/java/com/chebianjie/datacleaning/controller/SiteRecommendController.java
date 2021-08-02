package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.SiteRecommend;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.SiteRecommendService;
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
public class SiteRecommendController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    SiteRecommendService siteRecommendService;

    @Autowired
    AddConsumerUnionAccountLogService logService;



    @RequestMapping("/cleanCbjSiteRecommend")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjSiteRecommend(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<SiteRecommend> siteRecommendPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("page: {} size: {}", page, size);
            siteRecommendPage = siteRecommendService.getCbjAllSiteRecommend(pageable);
            totalPage = siteRecommendPage.getTotalPages();
            List<SiteRecommend> siteRecommendList = siteRecommendPage.getContent();
            for (int i=0;i<siteRecommendList.size();i++){
                SiteRecommend siteRecommend = siteRecommendList.get(i);
                Long id = siteRecommend.getId();
                Long cbjId = siteRecommend.getConsumerId();
                List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        siteRecommendService.updateCbjSiteRecommendById(consumerUnionAccount, id);
                        logService.saveOne(4,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(4,cbjId, null, null, 0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);

        return "end";
    }


//    @RequestMapping("/cleanChjSiteRecommend")
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public String cleanChjSiteRecommend(){
//        int totalPage;
//        int page = 0;
//        int size = 10;
//        Page<SiteRecommend> siteRecommendPage;
//        do {
//            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
//            log.info("page: {} size: {}", page, size);
//            siteRecommendPage = siteRecommendService.getChjAllSiteRecommend(pageable);
//            totalPage = siteRecommendPage.getTotalPages();
//            List<SiteRecommend> siteRecommendList = siteRecommendPage.getContent();
//            for (int i=0;i<siteRecommendList.size();i++){
//                SiteRecommend siteRecommend = siteRecommendList.get(i);
//                Long id = siteRecommend.getId();
//                Long chjId = siteRecommend.getConsumerId();
//                List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
//                if(consumerLogList.size()>0){
//                    Long consumerId = consumerLogList.get(0).getConsumerId();
//                    Consumer consumer = consumerService.findById(consumerId);
//                    if(consumer != null){
//                        String consumerUnionAccount = consumer.getUnionAccount();
//                        siteRecommendService.updateChjSiteRecommendById(consumerUnionAccount, id);
//                    }
//                }
//            }
//            if(totalPage == 0){
//                break;
//            }
//            page = page + 1;
//        }while (page!= totalPage);
//
//        return "end";
//    }










}
