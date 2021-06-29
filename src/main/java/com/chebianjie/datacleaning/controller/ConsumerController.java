package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import com.chebianjie.datacleaning.service.ChjUtConsumerService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.ConsumerService;
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
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController {

    @Autowired
    private CbjUtConsumerService cbjUtConsumerService;

    @Autowired
    private ChjUtConsumerService chjUtConsumerService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ConsumerLogService consumerLogService;

    /**
     * 同步迁移用户数据 - 以车便捷为主
     * @return
     */
    @GetMapping("/synch")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object synchConsumer(){
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(4);
        //同步开始
        int totalPages;
        int page = 690;
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
                    if(consumerLogService.getOneByUnionId(curCbjUtConsumer.getUnionid(), 1) != null){
                        continue;
                    }
                    //存在脏数据同一unionid有两个账号
                    List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByUnionid(curCbjUtConsumer.getUnionid());
                    if(chjUtConsumerList.size() > 1){
                        ConsumerLog temp = new ConsumerLog();
                        temp.setUnionid(curCbjUtConsumer.getUnionid());
                        temp.setCbjId(curCbjUtConsumer.getId());
                        temp.setStatus(0);
                        consumerLogService.saveOne(temp);

                        continue;
                    }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                        chjUtConsumer = chjUtConsumerList.get(0);
                    }
                }else if(StrUtil.isNotBlank(curCbjUtConsumer.getAccount())){
                    //检查是否搬迁过
                    if(consumerLogService.getOneByCbjAccount(curCbjUtConsumer.getAccount(), 1) != null){
                        continue;
                    }
                    //存在脏数据同一unionid有两个账号
                    List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(curCbjUtConsumer.getAccount());
                    if(chjUtConsumerList.size() > 1){
                        ConsumerLog temp = new ConsumerLog();
                        temp.setCbjAccount(curCbjUtConsumer.getAccount());
                        temp.setCbjId(curCbjUtConsumer.getId());
                        temp.setStatus(0);
                        consumerLogService.saveOne(temp);

                        continue;
                    }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                        chjUtConsumer = chjUtConsumerList.get(0);
                    }
                }
                //3.处理数据
                es.submit(new ConsumerTask(consumerService, curCbjUtConsumer, chjUtConsumer));
                //consumerService.mergeConsumer(curCbjUtConsumer, chjUtConsumer);
            }
            page = page +1;
        }while(page != totalPages);
        es.shutdown();

        return "finish";
    }

    /**
     * 同步迁移用户数据 - 以车惠捷为主
     * @return
     */
    @GetMapping("/synch2")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object synchConsumer2(){
        //创建线程
        //ExecutorService es = Executors.newFixedThreadPool(4);
        //同步开始
        int totalPages;
        int page = 0;
        int size = 10;
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
                ConsumerLog curConsumerLog = consumerLogService.getOneByUnionId(curChjUtConsumer.getUnionid(), 1);
                if(curConsumerLog == null) {
                    //4.处理数据
                    //es.submit(new ConsumerTask(curCbjUtConsumer, chjUtConsumer));
                    consumerService.mergeConsumer(null, curChjUtConsumer);
                }
            }
            page = page +1;
        }while(page != totalPages);
        //es.shutdown();

        return "finish";
    }

    @GetMapping("/test")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object testSynch(@RequestParam("id") long id){
        //1.获取车便捷用户
        UtConsumer curCbjUtConsumer = cbjUtConsumerService.getUtConsumerById(id);
        //2.与车惠捷用户对比  ---- 先用unionid合并,若unionid为空则用account
        UtConsumer chjUtConsumer = null;
        if (StrUtil.isNotBlank(curCbjUtConsumer.getUnionid())) {
            //检查是否搬迁过
            if (consumerLogService.getOneByUnionId(curCbjUtConsumer.getUnionid(), 1) != null) {
                return "exist unionid";
            }
            //存在脏数据同一unionid有两个账号
            List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByUnionid(curCbjUtConsumer.getUnionid());
            if (chjUtConsumerList.size() > 1) {
                //非unique返回记录不处理
                ConsumerLog temp = new ConsumerLog();
                temp.setUnionid(curCbjUtConsumer.getUnionid());
                temp.setCbjId(curCbjUtConsumer.getId());
                temp.setStatus(2);
                consumerLogService.saveOne(temp);

                return "not unique unionid";
            }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                chjUtConsumer = chjUtConsumerList.get(0);
            }
        } else if (StrUtil.isNotBlank(curCbjUtConsumer.getAccount())) {
            //检查是否搬迁过
            if (consumerLogService.getOneByCbjAccount(curCbjUtConsumer.getAccount(), 1) != null) {
                return "exist account";
            }
            //存在脏数据同一unionid有两个账号
            List<UtConsumer> chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(curCbjUtConsumer.getAccount());
            if (chjUtConsumerList.size() > 1) {
                //非unique返回记录不处理
                ConsumerLog temp = new ConsumerLog();
                temp.setCbjAccount(curCbjUtConsumer.getAccount());
                temp.setCbjId(curCbjUtConsumer.getId());
                temp.setStatus(2);
                consumerLogService.saveOne(temp);

                return "not unique account";
            }else if(CollectionUtil.isNotEmpty(chjUtConsumerList)){
                chjUtConsumer = chjUtConsumerList.get(0);
            }
        }
        //3.处理数据
        consumerService.mergeConsumer(curCbjUtConsumer, chjUtConsumer);

        return "test finish";
    }
}
