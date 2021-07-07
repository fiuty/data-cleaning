package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerBill;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;

/**
 * @author zhengdayue
 * @date: 2021-06-28
 */
public interface ConsumerBillService {

    void cleanOne(int pageNumber, int pageSize);

    void deleteFail();

    void consumerBillJob();

    ConsumerBill fillInfoConsumerBill(UtUserTotalFlow currentFlow, Consumer consumer);

    void addBatchClean(Consumer consumer);
}
