package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.service.ConsumerBillService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
@Data
@Slf4j
public class ConsumerBillThread extends Thread {

    private Consumer consumer;

    private ConsumerBillService consumerBillService;

    public ConsumerBillThread(Consumer consumer, ConsumerBillService consumerBillService) {
        this.consumer = consumer;
        this.consumerBillService = consumerBillService;
    }

    @Override
    public void run() {
        consumerBillService.threadClean(consumer);
    }
}
