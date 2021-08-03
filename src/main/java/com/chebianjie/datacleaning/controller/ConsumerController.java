package com.chebianjie.datacleaning.controller;

import com.chebianjie.datacleaning.config.DataCleanConfiguration;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.UtConsumerBak;
import com.chebianjie.datacleaning.threads.ConsumerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController extends AbstractBaseController{

    @Autowired
    private DataCleanConfiguration dataCleanConfiguration;

    /**
     * 同步迁移用户数据 - 以车便捷为主
     * @return
     */
    @GetMapping("/sync")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object syncConsumer(){
        long start = System.currentTimeMillis();
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(50);
        int total = cbjUtConsumerService.countByCreatetimeLessThanEqual(dataCleanConfiguration.getConsumerTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getConsumerStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtConsumer> utConsumers = cbjUtConsumerService.findAllByPage(pageNumber * pageSize, pageSize);
            utConsumers.forEach(curCbjUtConsumer ->{
                //判断是否为脏数据
                if(checkUtConsumer(curCbjUtConsumer, 1)) {
                    //针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
                    curCbjUtConsumer = fixUtConsumerByAccount(curCbjUtConsumer, 1);
                    //获取与当前车便捷用户对应的车惠捷用户
                    UtConsumer chjUtConsumer = getChjUtConsumerByCbjUtConsumer(curCbjUtConsumer);
                    //检查是否已经清洗
                    if (!checkCleanConsumer(curCbjUtConsumer, 1)) {
                        //6.处理数据
                        es.submit(new ConsumerTask(consumerService, consumerBalanceService, curCbjUtConsumer, chjUtConsumer));
                    }
                }else{
                    //脏数据转移备份表
                    UtConsumerBak temp = new UtConsumerBak();
                    BeanUtils.copyProperties(curCbjUtConsumer, temp);
                    temp.setId(null);
                    temp.setPlatform(1);
                    utConsumerBakService.save(temp);
                    log.info("[CBJ 脏数据] INSERT");
                }
            });
            Instant end = Instant.now();
            log.info("车便捷用户清洗,总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车便捷用户清洗,总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
        es.shutdown();

        return "finish: [" + (System.currentTimeMillis() - start) + "]";
    }

    /**
     * 同步迁移用户数据 - 以车惠捷为主
     * @return
     */
    @GetMapping("/sync2")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object syncConsumer2(){
        long start = System.currentTimeMillis();
        //创建线程
        ExecutorService es = Executors.newFixedThreadPool(50);
        int total = chjUtConsumerService.countByCreatetimeLessThanEqual(dataCleanConfiguration.getConsumerTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getConsumerStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtConsumer> utConsumers = chjUtConsumerService.findAllByPage(pageNumber * pageSize, pageSize);
            utConsumers.forEach(curChjUtConsumer->{
                //判断是否为脏数据
                if(checkUtConsumer(curChjUtConsumer, 2)) {
                    //针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
                    curChjUtConsumer = fixUtConsumerByAccount(curChjUtConsumer, 2);
                    //获取与当前车便捷用户对应的车惠捷用户
                    UtConsumer cbjUtConsumer = getCbjUtConsumerByChjUtConsumer(curChjUtConsumer);
                    //检查是否已经清洗
                    if (!checkCleanConsumer(curChjUtConsumer, 2)) {
                        //处理数据
                        es.submit(new ConsumerTask(consumerService, consumerBalanceService, cbjUtConsumer, curChjUtConsumer));
                    } else {
                        log.info("[CHJ SYNC] EXIST");
                    }
                }else{
                    //脏数据转移备份表
                    UtConsumerBak temp = new UtConsumerBak();
                    BeanUtils.copyProperties(curChjUtConsumer, temp);
                    temp.setId(null);
                    temp.setPlatform(2);
                    utConsumerBakService.save(temp);
                    log.info("[CHJ 脏数据] INSERT");
                }
            });
            Instant end = Instant.now();
            log.info("车惠捷用户清洗,总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车惠捷用户清洗,总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
        es.shutdown();

        return "[CHJ SYNC] finish [" + (System.currentTimeMillis() - start) + "]";
    }

    @GetMapping("/test")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object syncTest(@RequestParam("id") long id, @RequestParam("type") int type){
        //获取车便捷用户
        UtConsumer utConsumer =  type == 1 ? cbjUtConsumerService.getUtConsumerById(id) : chjUtConsumerService.getUtConsumerById(id);
        //针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
        utConsumer = fixUtConsumerByAccount(utConsumer, type);
        //获取与当前用户数据对应的另一用户数据
        UtConsumer fixUtConsumer = type == 1 ? getChjUtConsumerByCbjUtConsumer(utConsumer) : getCbjUtConsumerByChjUtConsumer(utConsumer);
        //检查是否已经清洗
        if(!checkCleanConsumer(utConsumer, type)){
            //处理数据
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

    /**
     * 补全数据 - 车便捷
     * @return
     */
    @GetMapping("/fix1")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object fixConsumer1(){
        long start = System.currentTimeMillis();
        //创建线程
        final ExecutorService es = Executors.newFixedThreadPool(50);
        int total = cbjUtConsumerService.countByCreateTimeGreaterThan(dataCleanConfiguration.getConsumerTime());
        int startPage = dataCleanConfiguration.getFixCbjConsumerStartPage();
        int totalPage = computeTotalPage(total) + startPage;
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = startPage; pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtConsumer> utConsumers = cbjUtConsumerService.findAllByPage(pageNumber * pageSize, pageSize);
            utConsumers.forEach(curCbjUtConsumer ->{
                //判断是否为脏数据
                if(checkUtConsumer(curCbjUtConsumer, 1)) {
                    //针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
                    curCbjUtConsumer = fixUtConsumerByAccount(curCbjUtConsumer, 1);
                    //获取与当前车便捷用户对应的车惠捷用户
                    UtConsumer chjUtConsumer = getChjUtConsumerByCbjUtConsumer(curCbjUtConsumer);
                    //检查是否已清洗
                    Consumer curConsumer = consumerService.findByPhone(curCbjUtConsumer.getAccount());
                    if(curConsumer != null) {
                        //清洗
                        log.info("[增量1] utConsumer : {}", curCbjUtConsumer);
                        cleanFixConsumer(curCbjUtConsumer, 1);
                    }else{
                        //处理数据
                        log.info("[增量2] utConsumer : {}", curCbjUtConsumer);
                        es.submit(new ConsumerTask(consumerService, consumerBalanceService, curCbjUtConsumer, chjUtConsumer));
                    }
                }else{
                    //脏数据转移备份表
                    UtConsumerBak temp = new UtConsumerBak();
                    BeanUtils.copyProperties(curCbjUtConsumer, temp);
                    temp.setId(null);
                    temp.setPlatform(1);
                    utConsumerBakService.save(temp);
                    log.info("[CBJ 脏数据] INSERT");
                }
            });
            Instant end = Instant.now();
            log.info("车便捷用户清洗,总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车便捷用户清洗,总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
        es.shutdown();

        return "finish: [" + (System.currentTimeMillis() - start) + "]";
    }

    /**
     * 补全数据 - 车惠捷
     * @return
     */
    @GetMapping("/fix2")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object fixConsumer2(){
        long start = System.currentTimeMillis();
        //创建线程
        ExecutorService es = Executors.newFixedThreadPool(50);
        int total = chjUtConsumerService.countByCreateTimeGreaterThan(dataCleanConfiguration.getConsumerTime());
        int startPage = dataCleanConfiguration.getFixChjConsumerStartPage();
        int totalPage = computeTotalPage(total) + startPage;
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = startPage; pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtConsumer> utConsumers = chjUtConsumerService.findAllByPage(pageNumber * pageSize, pageSize);
            utConsumers.forEach(curChjUtConsumer->{
                //判断是否为脏数据
                if(checkUtConsumer(curChjUtConsumer, 2)) {
                    //针对存在同一account多条数据需提前合并 - 旧注册接口(utConsumerResource)限制account不重复
                    curChjUtConsumer = fixUtConsumerByAccount(curChjUtConsumer, 2);
                    //获取与当前车便捷用户对应的车惠捷用户
                    UtConsumer cbjUtConsumer = getCbjUtConsumerByChjUtConsumer(curChjUtConsumer);
                    //检查是否已清洗
                    Consumer curConsumer = consumerService.findByPhone(curChjUtConsumer.getAccount());
                    if(curConsumer != null) {
                        //清洗
                        log.info("[增量1] utConsumer : {}", curChjUtConsumer);
                        cleanFixConsumer(curChjUtConsumer, 2);
                    }else{
                        //处理数据
                        log.info("[增量2] utConsumer : {}", curChjUtConsumer);
                        es.submit(new ConsumerTask(consumerService, consumerBalanceService, cbjUtConsumer, curChjUtConsumer));
                    }
                }else{
                    //脏数据转移备份表
                    UtConsumerBak temp = new UtConsumerBak();
                    BeanUtils.copyProperties(curChjUtConsumer, temp);
                    temp.setId(null);
                    temp.setPlatform(2);
                    utConsumerBakService.save(temp);
                    log.info("[CHJ 脏数据] INSERT");
                }
            });
            Instant end = Instant.now();
            log.info("车惠捷用户清洗,总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车惠捷用户清洗,总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
        es.shutdown();

        return "[CHJ SYNC] finish [" + (System.currentTimeMillis() - start) + "]";
    }

    private int computeTotalPage(long total) {
        int pageSize = 1000;
        int totalPage = (int)total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        return totalPage;
    }
}
