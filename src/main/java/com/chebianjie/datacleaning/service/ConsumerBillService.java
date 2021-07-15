package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerBill;
import com.chebianjie.datacleaning.domain.ConsumerBillChangeDetail;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.enums.BalanceType;
import com.chebianjie.datacleaning.domain.enums.Platform;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-28
 */
public interface ConsumerBillService {

    void cleanOne(int pageNumber, int pageSize);

    void deleteFail();

    void consumerBillJob();

    ConsumerBill fillInfoConsumerBill(UtUserTotalFlow currentFlow, Consumer consumer);

    List<ConsumerBillChangeDetail> handleBillDetail(ConsumerBill consumerBill, UtUserTotalFlow flow);

    Boolean isBalanceChange(ConsumerBill consumerBill);

    ConsumerBillChangeDetail fillInfoChangeDetail(String billIdentify, Integer afterChangeValue, Integer changeValue, BalanceType balanceType, Platform platform);

    void cleanOneConsumer(Long id);

    void handleFail();
}
