package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.threads.ConsumerTask;
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
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController extends AbstractBaseController{

    /**
     * 同步迁移用户数据 - 以车便捷为主
     * @return
     */
    @GetMapping("/synch")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object synchConsumer(){
        long start = System.currentTimeMillis();
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
            //2.处理数据
            for(int i = 1; i <= utConsumerList.size(); i++){
                UtConsumer curCbjUtConsumer = utConsumerList.get(i-1);
                //3.针对存在同一unionid多条数据需提前合并
                curCbjUtConsumer = fixCbjUtConsumer(curCbjUtConsumer);
                //4.获取与当前车便捷用户对应的车惠捷用户
                UtConsumer chjUtConsumer = fixChjUtConsumer(curCbjUtConsumer);
                //5.检查是否已经清洗
                if(!checkCleanConsumer(curCbjUtConsumer, chjUtConsumer)){
                    //6.处理数据
                    es.submit(new ConsumerTask(consumerService, consumerBalanceService, curCbjUtConsumer, chjUtConsumer));
                }
            }
            page = page +1;
        }while(page != totalPages);
        es.shutdown();

        return "finish: [" + (System.currentTimeMillis() - start) + "]";
    }

    /**
     * 同步迁移用户数据 - 以车惠捷为主
     * @return
     *//*
    @GetMapping("/synch2")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object synchConsumer2(){
        //创建线程
        ExecutorService es = Executors.newFixedThreadPool(30);
        //同步开始
        int totalPages;
        int page = 0;
        int size = 1000;
        Page<UtConsumer> utConsumerPage;
        do{
            //1.获取车惠捷用户
            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            log.info("page: {} size: {}", page, size);
            utConsumerPage = chjUtConsumerService.pageUtConsumer(pageRequest);
            totalPages = utConsumerPage.getTotalPages();
            List<UtConsumer> utConsumerList = utConsumerPage.getContent();
            for(int i = 1; i <= utConsumerList.size(); i++){
                //2.判断是否已搬迁
                UtConsumer curChjUtConsumer = utConsumerList.get(i-1);
                //3.查找成功迁移记录
                ConsumerLog curConsumerLog = consumerLogService.getOneByUnionId(curChjUtConsumer.getUnionid(), 1, 1);
                if(curConsumerLog == null) {
                    //4.处理数据
                    //es.submit(new ConsumerTask(curCbjUtConsumer, chjUtConsumer));
                    consumerService.mergeConsumer(null, curChjUtConsumer);
                }
            }
            page = page +1;
        }while(page != totalPages);
        es.shutdown();

        return "finish";
    }*/

    @GetMapping("/test")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object testSynch(@RequestParam("id") long id){
        //1.获取车便捷用户
        UtConsumer curCbjUtConsumer = cbjUtConsumerService.getUtConsumerById(id);
        //3.针对存在同一unionid多条数据需提前合并
        curCbjUtConsumer = fixCbjUtConsumer(curCbjUtConsumer);
        //4.获取与当前车便捷用户对应的车惠捷用户
        UtConsumer chjUtConsumer = fixChjUtConsumer(curCbjUtConsumer);
        //5.检查是否已经清洗
        if(!checkCleanConsumer(curCbjUtConsumer, chjUtConsumer)){
            //3.处理数据
            Consumer consumer = consumerService.mergeConsumer(curCbjUtConsumer, chjUtConsumer);
            if(consumer != null){
                //迁移用户余额
                consumerBalanceService.mergeByConsumer(consumer, curCbjUtConsumer, chjUtConsumer);
            }
        }


        return "test finish";
    }
}
