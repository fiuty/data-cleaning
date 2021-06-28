package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class ConsumerTask implements Runnable {

    @Autowired
    private ConsumerService consumerService;

    //private String name;

    private UtConsumer cbjUtConsumer;

    private UtConsumer chjUtConsumer;

    public ConsumerTask(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        this.cbjUtConsumer = cbjUtConsumer;
        this.chjUtConsumer = chjUtConsumer;
    }

    @Override
    public void run() {
        consumerService.mergeConsumer(cbjUtConsumer, chjUtConsumer);
    }
}
