package com.chebianjie.datacleaning.controller;


import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.StaffInviteDetail;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.StaffInviteDetailService;
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
public class StaffInviteDetailController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    StaffInviteDetailService staffInviteDetailService;

    @Autowired
    AddConsumerUnionAccountLogService logService;


    @RequestMapping("/cleanCbjStaffInviteDetail")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjStaffInviteDetail(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<StaffInviteDetail> staffInviteDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【staff_invite_detail】 page: {} size: {}", page, size);
            staffInviteDetailPage = staffInviteDetailService.getCbjAllStaffInviteDetail(pageable);
            totalPage = staffInviteDetailPage.getTotalPages();
            List<StaffInviteDetail> staffInviteDetailList = staffInviteDetailPage.getContent();
            for (int i=0;i<staffInviteDetailList.size();i++){
                StaffInviteDetail staffInviteDetail = staffInviteDetailList.get(i);
                Long id = staffInviteDetail.getId();
                Long cbjId = staffInviteDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = cbjId != null?consumerLogService.getCbjConsumerLogByConsumerId(cbjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        staffInviteDetailService.updateCbjStaffInviteDetailById(consumerUnionAccount, id);
                        logService.saveOne(5,id,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(5, id, cbjId, null, null, 0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page!= totalPage);

        return "end";
    }


    @RequestMapping("/cleanChjStaffInviteDetail")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanChjStaffInviteDetail(){
        int totalPage;
        int page = 0;
        int size = 10;
        Page<StaffInviteDetail> staffInviteDetailPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("chj【staff_invite_detail】page: {} size: {}", page, size);
            staffInviteDetailPage = staffInviteDetailService.getChjAllStaffInviteDetail(pageable);
            totalPage = staffInviteDetailPage.getTotalPages();
            List<StaffInviteDetail> staffInviteDetailList = staffInviteDetailPage.getContent();
            for (int i=0;i<staffInviteDetailList.size();i++){
                StaffInviteDetail staffInviteDetail = staffInviteDetailList.get(i);
                Long id = staffInviteDetail.getId();
                Long chjId = staffInviteDetail.getConsumerId();
                List<ConsumerLog> consumerLogList = chjId != null?consumerLogService.getChjConsumerLogByConsumerId(chjId):new ArrayList<>();
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        staffInviteDetailService.updateChjStaffInviteDetailById(consumerUnionAccount, id);
                        logService.saveOne(5,id,null,chjId,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(5,id, null,chjId, null, 0);
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
