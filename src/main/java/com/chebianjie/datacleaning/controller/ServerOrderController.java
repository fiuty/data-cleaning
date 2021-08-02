package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.ServerOrder;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
import com.chebianjie.datacleaning.service.ServerOrderService;
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
public class ServerOrderController {

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ServerOrderService serverOrderService;

    @Autowired
    AddConsumerUnionAccountLogService logService;





    @RequestMapping("/cleanCbjServerOrder")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String cleanCbjServerOrder() {
        int totalPage;
        int page = 0;
        int size = 10;
        Page<ServerOrder> serverOrderPage;
        do {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("cbj【server_order】page: {} size: {}", page, size);
            serverOrderPage = serverOrderService.getCbjAllServerOrder(pageable);
            totalPage = serverOrderPage.getTotalPages();
            List<ServerOrder> utCouponRefundList = serverOrderPage.getContent();
            for(int i=0;i < utCouponRefundList.size();i++){
                ServerOrder serverOrder = utCouponRefundList.get(i);
                Long id = serverOrder.getId();
                Long cbjId = serverOrder.getConsumerId();
                List<ConsumerLog> consumerLogList = consumerLogService.getCbjConsumerLogByConsumerId(cbjId);
                if(consumerLogList.size()>0){
                    Long consumerId = consumerLogList.get(0).getConsumerId();
                    Consumer consumer = consumerService.findById(consumerId);
                    if(consumer != null){
                        String consumerUnionAccount = consumer.getUnionAccount();
                        serverOrderService.updateCbjServerOrderById(consumerUnionAccount, id);
                        logService.saveOne(3,cbjId,null,consumerUnionAccount,1);
                    }
                }else {
                        logService.saveOne(3, cbjId, null, null, 0);
                }
            }
            if(totalPage == 0){
                break;
            }
            page = page + 1;
        }while (page != totalPage);
        return "end";
    }


//    @RequestMapping("/cleanChjServerOrder")
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public String cleanChjServerOrder() {
//        int totalPage;
//        int page = 0;
//        int size = 10;
//        Page<ServerOrder> serverOrderPage;
//        do {
//            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
//            log.info("chj【server_order】page: {} size: {}", page, size);
//            serverOrderPage = serverOrderService.getChjAllServerOrder(pageable);
//            totalPage = serverOrderPage.getTotalPages();
//            List<ServerOrder> utCouponRefundList = serverOrderPage.getContent();
//            for(int i=0;i < utCouponRefundList.size();i++){
//                ServerOrder serverOrder = utCouponRefundList.get(i);
//                Long id = serverOrder.getId();
//                Long chjId = serverOrder.getConsumerId();
//                List<ConsumerLog> consumerLogList = consumerLogService.getChjConsumerLogByConsumerId(chjId);
//                if(consumerLogList.size()>0){
//                    Long consumerId = consumerLogList.get(0).getConsumerId();
//                    Consumer consumer = consumerService.findById(consumerId);
//                    if(consumer != null){
//                        String consumerUnionAccount = consumer.getUnionAccount();
//                        serverOrderService.updateChjServerOrderById(consumerUnionAccount, id);
//                    }
//                }
//            }
//            if(totalPage == 0){
//                break;
//            }
//            page = page + 1;
//        }while (page != totalPage);
//        return "end";
//    }









}
