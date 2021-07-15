package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.ConsumerBill;
import com.chebianjie.datacleaning.domain.ConsumerBillChangeDetail;
import com.chebianjie.datacleaning.domain.FlowLog;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-13
 */
public interface BatchSaveService {

    void firstSaveAll(ConsumerBill consumerBill, List<ConsumerBillChangeDetail> consumerBillChangeDetails, FlowLog flowLog);

    void addSaveAll(ConsumerBill consumerBill, List<ConsumerBillChangeDetail> consumerBillChangeDetails, AddBillLog addBillLog, FlowLog flowLog);

    void firstBatchSaveAll(List<ConsumerBill> consumerBills, List<ConsumerBillChangeDetail> consumerBillChangeDetails, List<FlowLog> flowLogs);

    void addBatchSaveAll(List<ConsumerBill> consumerBills, List<ConsumerBillChangeDetail> consumerBillChangeDetails, List<AddBillLog> addBillLogs, List<FlowLog> flowLogs);


}
