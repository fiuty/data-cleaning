package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.ConsumerBalanceService;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.Data;

@Data
public class ConsumerBalanceTask implements Runnable {

    private ConsumerBalanceService consumerBalanceService;

    //private String name;

    private UtConsumer cbjUtConsumer;

    private UtConsumer chjUtConsumer;

    public ConsumerBalanceTask(ConsumerBalanceService consumerBalanceService, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        this.consumerBalanceService = consumerBalanceService;
        this.cbjUtConsumer = cbjUtConsumer;
        this.chjUtConsumer = chjUtConsumer;
    }

    @Override
    public void run() {
        consumerBalanceService.merge(cbjUtConsumer, chjUtConsumer);
    }
}
