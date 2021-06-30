package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.ConsumerBalanceService;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.Data;

@Data
public class ConsumerTask implements Runnable {

    private ConsumerService consumerService;

    private ConsumerBalanceService consumerBalanceService;

    //private String name;

    private UtConsumer cbjUtConsumer;

    private UtConsumer chjUtConsumer;

    public ConsumerTask(ConsumerService consumerService, ConsumerBalanceService consumerBalanceService, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        this.consumerService = consumerService;
        this.consumerBalanceService = consumerBalanceService;
        this.cbjUtConsumer = cbjUtConsumer;
        this.chjUtConsumer = chjUtConsumer;
    }

    @Override
    public void run() {

        //迁移用户
        Consumer consumer = consumerService.mergeConsumer(cbjUtConsumer, chjUtConsumer);
        if(consumer != null){
            //迁移用户余额
            consumerBalanceService.mergeByConsumer(consumer, cbjUtConsumer, chjUtConsumer);
        }
    }
}
