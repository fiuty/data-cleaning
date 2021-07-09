package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.ConsumerCarsLogRepository;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import com.chebianjie.datacleaning.repository.ConsumerRepository;
import com.chebianjie.datacleaning.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
@Slf4j
public class ConsumerCarsController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    ConsumerCarsService consumerCarsService;

    @Autowired
    ConsumerLogService consumerLogService;

    @Autowired
    ConsumerCarsLogService consumerCarsLogService;

    @GetMapping("/sysnCbjUtUserCars")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getCbjUtUserCars() {
        int totalPage;
        int page=0;
        int size=10;
        Page<UtUserCars> utUserCarsPage;
        do{
            //1.获取车便捷车主认证数据
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            log.info("page: {} size: {}", page, size);
            utUserCarsPage = consumerCarsService.getUtUserCarsByCbj(pageable);  //车便捷
            totalPage = utUserCarsPage.getTotalPages(); //总页数
            List<UtUserCars> cbjUserCarsList=utUserCarsPage.getContent(); //data
            for (int i = 0; i < cbjUserCarsList.size(); i++){
                UtUserCars cbjUtUserCars = cbjUserCarsList.get(i);
                String unionAccount = null;
                ConsumerCarsLog log=new ConsumerCarsLog();
                if (cbjUtUserCars.getUid() != null){
                    UtConsumer utConsumer = consumerCarsService.getCbjUtConsumerById(cbjUtUserCars.getUid()); //获取车便捷的用户
                    if(utConsumer == null || cbjUtUserCars.getCarDetailId() == null) {
                        log.setUserCarsId(cbjUtUserCars.getId());
                        log.setUid(cbjUtUserCars.getUid());
                        log.setPlatform(Platform.CHEBIANJIE);
                        log.setStatus(0);
                        log.setCreatTime(LocalDateTime.now());
                        log.setCarDetailId(cbjUtUserCars.getCarDetailId());
                        consumerCarsLogService.saveOne(log);
                        continue;
                    }
                    String phone = utConsumer.getAccount(); //用户手机号
                    String unionId = utConsumer.getUnionid();
                    Consumer consumer = consumerCarsService.getConsumerByPhone(phone);
                    if(consumer != null){
                        Long consumerId=consumer.getId();
                        unionAccount = consumer.getUnionAccount();  //消费者唯一账号
                        consumerCarsService.saveConsumerCars(cbjUtUserCars,consumerId,phone,unionAccount,unionId,Platform.CHEBIANJIE);
                        continue;
                    }

                    if(unionId !=null) {
                        Consumer consumer2 = consumerCarsService.getConsumerByUnionid(unionId);
                        if(consumer2 != null) {
                            Long consumerId=consumer2.getId();
                            unionAccount = consumer2.getUnionAccount();  //消费者唯一账号
                            consumerCarsService.saveConsumerCars(cbjUtUserCars,consumerId, phone, unionAccount, unionId, Platform.CHEBIANJIE);
                            continue;
                        }
                    }


                    log.setUserCarsId(cbjUtUserCars.getId());
                    log.setUid(cbjUtUserCars.getUid());
                    log.setPlatform(Platform.CHEBIANJIE);
                    log.setAccount(phone);
                    log.setStatus(0);
                    log.setCreatTime(LocalDateTime.now());
                    log.setCarDetailId(cbjUtUserCars.getCarDetailId());
                    consumerCarsLogService.saveOne(log);


                }


            }

            page = page +1;
        }while (page != totalPage);

        return "end";
    }


    @GetMapping("/sysnChjUtUserCars")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getChjUtUserCars(){
        //创建线程
        //final ExecutorService es = Executors.newFixedThreadPool(20);

        int totalPage;
        int page=0;
        int size=10;
        Page<UtUserCars> utUserCarsPage;
        do {
            //1.获取车惠捷车主认证数据
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
            log.info("page: {} size: {}", page, size);
            utUserCarsPage = consumerCarsService.getUtUserCarsByChj(pageable);  //车惠捷
            totalPage = utUserCarsPage.getTotalPages(); //总页数
            List<UtUserCars> chjUserCarsList = utUserCarsPage.getContent(); //data
            for (int i = 0; i < chjUserCarsList.size(); i++) {
                UtUserCars chjUtUserCars = chjUserCarsList.get(i);
                String unionAccount = null;
                ConsumerCarsLog log=new ConsumerCarsLog();
                if (chjUtUserCars.getUid() != null) {
                    UtConsumer utConsumer = consumerCarsService.getChjUtConsumerById(chjUtUserCars.getUid());

                    if(utConsumer == null || chjUtUserCars.getCarDetailId() == null) {
                        log.setUserCarsId(chjUtUserCars.getId());
                        log.setUid(chjUtUserCars.getUid());
                        log.setPlatform(Platform.CHEHUIJIE);
                        log.setStatus(0);
                        log.setCreatTime(LocalDateTime.now());
                        log.setCarDetailId(chjUtUserCars.getCarDetailId());
                        consumerCarsLogService.saveOne(log);
                        continue;
                    }
                    String phone = utConsumer.getAccount(); //用户手机号
                    String unionId=utConsumer.getUnionid(); //Unionid
                    Consumer consumer = consumerCarsService.getConsumerByPhone(phone);
                    if(consumer != null){
                        Long consumerId=consumer.getId();
                        unionAccount = consumer.getUnionAccount();  //消费者唯一账号
                        consumerCarsService.saveConsumerCars(chjUtUserCars,consumerId,phone,unionAccount,unionId,Platform.CHEHUIJIE);
                        continue;
                    }

                    if(unionId !=null) {
                        Consumer consumer2 = consumerCarsService.getConsumerByUnionid(unionId);
                        if(consumer2 != null) {
                            Long consumerId=consumer2.getId();
                            unionAccount = consumer2.getUnionAccount();  //消费者唯一账号
                            consumerCarsService.saveConsumerCars(chjUtUserCars,consumerId, phone, unionAccount, unionId, Platform.CHEHUIJIE);
                            continue;
                        }
                    }

                    log.setUserCarsId(chjUtUserCars.getId());
                    log.setUid(chjUtUserCars.getUid());
                    log.setPlatform(Platform.CHEHUIJIE);
                    log.setAccount(phone);
                    log.setStatus(0);
                    log.setCreatTime(LocalDateTime.now());
                    log.setCarDetailId(chjUtUserCars.getCarDetailId());
                    consumerCarsLogService.saveOne(log);

                }

            }

            page = page+1;
        }while (page != totalPage);
        //es.shutdown();

        return "end";
    }







}
