package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.ConsumerBalance;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import com.chebianjie.datacleaning.service.ChjUtConsumerService;
import com.chebianjie.datacleaning.service.ConsumerBalanceService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.threads.ConsumerBalanceTask;
import com.chebianjie.datacleaning.threads.ConsumerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/consumer-balance")
@Slf4j
public class ConsumerBalanceController {

    @Autowired
    private CbjUtConsumerService cbjUtConsumerService;

    @Autowired
    private ChjUtConsumerService chjUtConsumerService;

    @Autowired
    private ConsumerLogService consumerLogService;

    @Autowired
    private ConsumerBalanceService consumerBalanceService;

    @GetMapping("/synch")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object synchConsumerBalance(){

        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(50);
        //同步开始
        int totalPages;
        int page = 0;
        int size = 1000;
        Page<UtConsumer> utConsumerPage;
        do{
            //1.获取车便捷用户
            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            log.info("page: {} size: {}", page, size);
            utConsumerPage = cbjUtConsumerService.pageUtConsumer(pageRequest);
            totalPages = utConsumerPage.getTotalPages();
            List<UtConsumer> utConsumerList = utConsumerPage.getContent();
            for(int i = 1; i <= utConsumerList.size(); i++){
                //2.与车惠捷用户对比  ---- 先用unionid合并,若unionid为空则用account
                UtConsumer curCbjUtConsumer = utConsumerList.get(i-1);
                UtConsumer chjUtConsumer = null;
                if(StrUtil.isNotBlank(curCbjUtConsumer.getUnionid())){
                    //检查是否搬迁过
                    if(consumerLogService.getOneByUnionId(curCbjUtConsumer.getUnionid(), 2, 1) != null){
                        continue;
                    }
                    //存在脏数据同一unionid有两个账号
                    List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByUnionid(curCbjUtConsumer.getUnionid());
                    if(chjUtConsumerList.size() > 1){
                        ConsumerLog temp = new ConsumerLog();
                        temp.setUnionid(curCbjUtConsumer.getUnionid());
                        temp.setCbjId(curCbjUtConsumer.getId());
                        temp.setType(2);
                        temp.setStatus(0);
                        consumerLogService.saveOne(temp);

                        continue;
                    }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                        chjUtConsumer = chjUtConsumerList.get(0);
                    }
                }else if(StrUtil.isNotBlank(curCbjUtConsumer.getAccount())){
                    //检查是否搬迁过
                    if(consumerLogService.getOneByCbjAccount(curCbjUtConsumer.getAccount(), 2, 1) != null){
                        continue;
                    }
                    //存在脏数据同一unionid有两个账号
                    List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(curCbjUtConsumer.getAccount());
                    if(chjUtConsumerList.size() > 1){
                        ConsumerLog temp = new ConsumerLog();
                        temp.setCbjAccount(curCbjUtConsumer.getAccount());
                        temp.setCbjId(curCbjUtConsumer.getId());
                        temp.setType(2);
                        temp.setStatus(0);
                        consumerLogService.saveOne(temp);

                        continue;
                    }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                        chjUtConsumer = chjUtConsumerList.get(0);
                    }
                }
                //3.处理数据
                es.submit(new ConsumerBalanceTask(consumerBalanceService, curCbjUtConsumer, chjUtConsumer));
            }
            page = page +1;
        }while(page != totalPages);
        es.shutdown();

        return "finish";
    }

    @GetMapping("/test")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object testSynchConsumerBalance(@RequestParam("id") long id){
        //1.获取车便捷用户
        UtConsumer curCbjUtConsumer = cbjUtConsumerService.getUtConsumerById(id);
        //2.与车惠捷用户对比  ---- 先用unionid合并,若unionid为空则用account
        UtConsumer chjUtConsumer = null;
        if (StrUtil.isNotBlank(curCbjUtConsumer.getUnionid())) {
            //检查是否搬迁过
            if (consumerLogService.countByUnionId(curCbjUtConsumer.getUnionid(), 2, 1) > 0) {
                return "exist unionid balance";
            }
            //存在脏数据同一unionid有两个账号
            List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByUnionid(curCbjUtConsumer.getUnionid());
            if(chjUtConsumerList.size() > 1){
                ConsumerLog temp = new ConsumerLog();
                temp.setUnionid(curCbjUtConsumer.getUnionid());
                temp.setCbjId(curCbjUtConsumer.getId());
                temp.setType(2);
                temp.setStatus(0);
                consumerLogService.saveOne(temp);

                return "not unique";
            }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                chjUtConsumer = chjUtConsumerList.get(0);
            }
        } else if (StrUtil.isNotBlank(curCbjUtConsumer.getAccount())) {
            //检查是否搬迁过
            if (consumerLogService.countByCbjAccount(curCbjUtConsumer.getAccount(), 2, 1) > 0) {
                return "exist account balance";
            }
            //存在脏数据同一unionid有两个账号
            List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(curCbjUtConsumer.getAccount());
            if(chjUtConsumerList.size() > 1){
                ConsumerLog temp = new ConsumerLog();
                temp.setCbjAccount(curCbjUtConsumer.getAccount());
                temp.setCbjId(curCbjUtConsumer.getId());
                temp.setType(2);
                temp.setStatus(0);
                consumerLogService.saveOne(temp);

                return "exist account balance";
            }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                chjUtConsumer = chjUtConsumerList.get(0);
            }
        }
        //3.处理数据
        consumerBalanceService.merge(curCbjUtConsumer, chjUtConsumer);

        return "test finish";
    }
}
