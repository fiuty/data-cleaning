package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.Data;

@Data
public class ConsumerTask implements Runnable {

    private ConsumerService consumerService;

    //private String name;

    private UtConsumer cbjUtConsumer;

    private UtConsumer chjUtConsumer;

    public ConsumerTask(ConsumerService consumerService, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        this.consumerService = consumerService;
        this.cbjUtConsumer = cbjUtConsumer;
        this.chjUtConsumer = chjUtConsumer;
    }

    @Override
    public void run() {
        consumerService.mergeConsumer(cbjUtConsumer, chjUtConsumer);
    }
}
