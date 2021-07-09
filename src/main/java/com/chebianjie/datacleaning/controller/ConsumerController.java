package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.domain.Consumer;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @GetMapping("/sync")
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
            //2.开始遍历
            for(int i = 1; i <= utConsumerList.size(); i++){
                UtConsumer curCbjUtConsumer = utConsumerList.get(i-1);
                //3.针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
                curCbjUtConsumer = fixUtConsumerByAccount(curCbjUtConsumer, 1);
                //4.获取与当前车便捷用户对应的车惠捷用户
                UtConsumer chjUtConsumer = getChjUtConsumerByCbjUtConsumer(curCbjUtConsumer);
                //5.检查是否已经清洗
                if(!checkCleanConsumer(curCbjUtConsumer, 1)){
                    //6.处理数据
                    es.submit(new ConsumerTask(consumerService, consumerBalanceService, curCbjUtConsumer, chjUtConsumer));
                }
            }
            page = page + 1;
        }while(page != totalPages);
        es.shutdown();

        return "finish: [" + (System.currentTimeMillis() - start) + "]";
    }

    /**
     * 同步迁移用户数据 - 以车惠捷为主
     * @return
     */
    @GetMapping("/sync2")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object synchConsumer2(){
        //创建线程
        ExecutorService es = Executors.newFixedThreadPool(50);
        //同步开始
        int totalPages;
        int page = 0;
        int size = 1000;
        Page<UtConsumer> utConsumerPage;
        do{
            //1.获取车便捷用户
            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
            log.info("page: {} size: {}", page, size);
            utConsumerPage = chjUtConsumerService.pageUtConsumer(pageRequest);
            totalPages = utConsumerPage.getTotalPages();
            List<UtConsumer> utConsumerList = utConsumerPage.getContent();
            //2.开始遍历
            for(int i = 1; i <= utConsumerList.size(); i++){
                UtConsumer curChjUtConsumer = utConsumerList.get(i-1);
                //3.针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
                curChjUtConsumer = fixUtConsumerByAccount(curChjUtConsumer, 2);
                //4.获取与当前车便捷用户对应的车惠捷用户
                UtConsumer cbjUtConsumer = getCbjUtConsumerByChjUtConsumer(curChjUtConsumer);
                //5.检查是否已经清洗
                if(!checkCleanConsumer(curChjUtConsumer, 2)){
                    //6.处理数据
                    es.submit(new ConsumerTask(consumerService, consumerBalanceService, cbjUtConsumer, curChjUtConsumer));
                }else{
                    log.info("[CHJ SYNC] EXIST");
                }
            }
        }while(page != totalPages);
        es.shutdown();

        return "finish";
    }

    @GetMapping("/test")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object testSynch(@RequestParam("id") long id, @RequestParam("type") int type){
        //1.获取车便捷用户
        UtConsumer utConsumer =  type == 1 ? cbjUtConsumerService.getUtConsumerById(id) : chjUtConsumerService.getUtConsumerById(id);
        //3.针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
        utConsumer = fixUtConsumerByAccount(utConsumer, type);
        //4.获取与当前用户数据对应的另一用户数据
        UtConsumer fixUtConsumer = type == 1 ? getChjUtConsumerByCbjUtConsumer(utConsumer) : getCbjUtConsumerByChjUtConsumer(utConsumer);
        //5.检查是否已经清洗
        if(!checkCleanConsumer(utConsumer, type)){
            //3.处理数据
            Consumer consumer = type == 1 ? consumerService.mergeConsumer(utConsumer, fixUtConsumer) : consumerService.mergeConsumer(fixUtConsumer, utConsumer);
            if(consumer != null){
                //迁移用户余额
                if(type == 1){
                    consumerBalanceService.mergeByConsumer(consumer, utConsumer, fixUtConsumer);
                }else if (type == 2){
                    consumerBalanceService.mergeByConsumer(consumer, fixUtConsumer, utConsumer);
                }
            }
        }else{
            return "exist";
        }

        return "test finish";
    }
}
