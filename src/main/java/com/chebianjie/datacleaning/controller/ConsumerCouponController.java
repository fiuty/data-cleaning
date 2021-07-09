package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.threads.ConsumerCouponTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/coupon")
@Slf4j
public class ConsumerCouponController extends AbstractBaseController {

    @GetMapping("/sync-cbj")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object syncCbjCoupon(@RequestParam("page") int page) {
        long start = System.currentTimeMillis();
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(50);
        //同步开始
        int totalPages;
        int size = 1000;
        Page<UtCouponUser> utCouponUserPage;
        do {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            log.info("page: {} size: {}", page, size);
            utCouponUserPage = utCouponUserService.pageCbjUtCouponUser(pageRequest);
            totalPages = utCouponUserPage.getTotalPages();
            List<UtCouponUser> utCouponUserList = utCouponUserPage.getContent();
            //2.处理数据
            for (int i = 1; i <= utCouponUserList.size(); i++) {
                UtCouponUser utCouponUser = utCouponUserList.get(i - 1);
                //3.线上utCouponUser存在consumer_account为null数据,因此判断只用consumer_id
                if (!checkCleanCoupon(utCouponUser, 1)) {
                    //4.获取当前关联utConsumer搬迁日志(consumerLog)
                    ConsumerLog curConsumerLog = consumerLogService.getOneByCbjIdAndStatusAndType(utCouponUser.getConsumerId(), 1, 1);
                    if (curConsumerLog == null) {
                        ConsumerCouponLog temp = new ConsumerCouponLog();
                        temp.setType(1);
                        temp.setStatus(0);
                        temp.setUtCouponUserId(utCouponUser.getId());

                        consumerCouponLogService.saveOne(temp);
                        continue;
                    }
                    //5.根据旧用户数据查找新用户数据
                    Consumer consumer = consumerService.getByConsumerLog(curConsumerLog);
                    // 新用户数据为空插入失败log
                    if (consumer == null) {
                        ConsumerCouponLog temp = new ConsumerCouponLog();
                        temp.setType(1);
                        temp.setStatus(0);
                        temp.setUtCouponUserId(utCouponUser.getId());

                        consumerCouponLogService.saveOne(temp);
                        continue;
                    }
                    //6..处理数据
                    es.submit(new ConsumerCouponTask(utCouponUserService, consumerCouponLogService, utCouponUser, consumer, 1));
                }
            }
            page = page + 1;
        } while (page + 1 != totalPages);

        return "finish: [" + (System.currentTimeMillis() - start) + "]";
    }

    @GetMapping("/sync-chj")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object syncChjCoupon() {
        long start = System.currentTimeMillis();
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(50);
        //同步开始
        int totalPages;
        int page = 0;
        int size = 1000;
        Page<UtCouponUser> utCouponUserPage;
        do {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            log.info("page: {} size: {}", page, size);
            utCouponUserPage = utCouponUserService.pageChjUtCouponUser(pageRequest);
            totalPages = utCouponUserPage.getTotalPages();
            List<UtCouponUser> utCouponUserList = utCouponUserPage.getContent();
            //2.处理数据
            for (int i = 1; i <= utCouponUserList.size(); i++) {
                UtCouponUser utCouponUser = utCouponUserList.get(i - 1);
                //3.线上utCouponUser存在consumer_account为null数据,因此判断只用consumer_id
                if (!checkCleanCoupon(utCouponUser, 2)) {
                    //4.获取当前关联utConsumer搬迁日志(consumerLog)
                    //log.info("param: [{}]", utCouponUser.getConsumerId());
                    ConsumerLog curConsumerLog = consumerLogService.getOneByChjIdAndStatusAndType(utCouponUser.getConsumerId(), 1, 1);
                    if (curConsumerLog == null) {
                        ConsumerCouponLog temp = new ConsumerCouponLog();
                        temp.setType(2);
                        temp.setStatus(0);
                        temp.setUtCouponUserId(utCouponUser.getId());

                        consumerCouponLogService.saveOne(temp);
                        continue;
                    }
                    //5.根据旧用户数据查找新用户数据
                    Consumer consumer = consumerService.getByConsumerLog(curConsumerLog);
                    // 新用户数据为空插入失败log
                    if (consumer == null) {
                        ConsumerCouponLog temp = new ConsumerCouponLog();
                        temp.setType(2);
                        temp.setStatus(0);
                        temp.setUtCouponUserId(utCouponUser.getId());

                        consumerCouponLogService.saveOne(temp);
                        continue;
                    }
                    //6..处理数据
                    es.submit(new ConsumerCouponTask(utCouponUserService, consumerCouponLogService, utCouponUser, consumer, 2));
                }
            }
            page = page + 1;
        } while (page + 1 != totalPages);

        return "finish: [" + (System.currentTimeMillis() - start) + "]";
    }

    @GetMapping("/sync-single")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object syncSingleCoupon(@RequestParam("id") long id, @RequestParam("type") int type) {
        //2.处理数据
        UtCouponUser utCouponUser = type == 1 ? utCouponUserService.getCbjOneById(id) : utCouponUserService.getChjOneById(id);
        //3.线上utCouponUser存在consumer_account为null数据,因此判断只用consumer_id
        if (!checkCleanCoupon(utCouponUser, type)) {
            //4.获取当前关联utConsumer搬迁日志(consumerLog)
            ConsumerLog curConsumerLog = type == 1 ? consumerLogService.getOneByCbjIdAndStatusAndType(utCouponUser.getConsumerId(), 1, type) : consumerLogService.getOneByChjIdAndStatusAndType(utCouponUser.getConsumerId(), 1, type);
            if (curConsumerLog == null) {
                return "fail - consumerLog null";
            }
            //5.根据旧用户数据查找新用户数据
            Consumer consumer = consumerService.getByConsumerLog(curConsumerLog);
            // 新用户数据为空插入失败log
            if (consumer == null) {
                ConsumerCouponLog temp = new ConsumerCouponLog();
                temp.setType(type);
                temp.setStatus(0);
                temp.setUtCouponUserId(utCouponUser.getId());

                consumerCouponLogService.saveOne(temp);
                return "fail";
            }
            //6..处理数据
            try {
                if (type == 1) {
                    //补全union_account - 车便捷
                    utCouponUserService.mergeCbjConsumerCoupon(utCouponUser, consumer);
                    //3.迁移成功记录日志
                    //判断是否有失败记录, 有则更新 无则插入
                    ConsumerCouponLog curConsumerCouponLog = consumerCouponLogService.getOneByUtCouponUserIdAndStatusAndType(utCouponUser.getConsumerId(), 0, type);
                    if (curConsumerCouponLog != null) {
                        curConsumerCouponLog.setStatus(1);
                        consumerCouponLogService.saveOne(curConsumerCouponLog);
                    } else {
                        ConsumerCouponLog temp = new ConsumerCouponLog();
                        temp.setUtCouponUserId(utCouponUser.getId());
                        temp.setType(type);
                        temp.setStatus(1);
                        consumerCouponLogService.saveOne(temp);
                    }
                } else if (type == 2) {
                    //补全union_account - 车惠捷
                    utCouponUserService.mergeChjConsumerCoupon(utCouponUser, consumer);
                    //3.迁移成功记录日志
                    //判断是否有失败记录, 有则更新 无则插入
                    ConsumerCouponLog curConsumerCouponLog = consumerCouponLogService.getOneByUtCouponUserIdAndStatusAndType(utCouponUser.getConsumerId(), 0, type);
                    if (curConsumerCouponLog != null) {
                        curConsumerCouponLog.setStatus(1);
                        consumerCouponLogService.saveOne(curConsumerCouponLog);
                    } else {
                        ConsumerCouponLog temp = new ConsumerCouponLog();
                        temp.setUtCouponUserId(utCouponUser.getId());
                        temp.setType(type);
                        temp.setStatus(1);
                        consumerCouponLogService.saveOne(temp);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                //4. 有任何报错记录log 失败
                ConsumerCouponLog temp = new ConsumerCouponLog();
                temp.setUtCouponUserId(utCouponUser.getId());
                temp.setType(type);
                temp.setStatus(0);
                consumerCouponLogService.saveOne(temp);
            }
        }
        return "exist";
    }
}
