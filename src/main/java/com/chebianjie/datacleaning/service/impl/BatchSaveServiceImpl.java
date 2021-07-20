package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.common.core.util.CollectUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.ConsumerBill;
import com.chebianjie.datacleaning.domain.ConsumerBillChangeDetail;
import com.chebianjie.datacleaning.domain.FlowLog;
import com.chebianjie.datacleaning.repository.AddBillLogRepository;
import com.chebianjie.datacleaning.repository.ConsumerBillChangeDetailRepository;
import com.chebianjie.datacleaning.repository.ConsumerBillRepository;
import com.chebianjie.datacleaning.repository.FlowLogRepository;
import com.chebianjie.datacleaning.service.BatchSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-13
 */
@Service
@Slf4j
public class BatchSaveServiceImpl implements BatchSaveService {

    @Autowired
    private ConsumerBillRepository consumerBillRepository;

    @Autowired
    private ConsumerBillChangeDetailRepository consumerBillChangeDetailRepository;

    @Autowired
    private FlowLogRepository flowLogRepository;

    @Autowired
    private AddBillLogRepository addBillLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void firstBatchSaveAll(List<ConsumerBill> consumerBills, List<ConsumerBillChangeDetail> consumerBillChangeDetails, List<FlowLog> flowLogs) {
        consumerBillRepository.batchInsert(consumerBills);
        if (CollectUtil.collectionNotEmpty(consumerBillChangeDetails)) {
            consumerBillChangeDetailRepository.batchInsert(consumerBillChangeDetails);
        }
        flowLogRepository.batchInsert(flowLogs);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addSaveAll(ConsumerBill consumerBill, List<ConsumerBillChangeDetail> consumerBillChangeDetails, AddBillLog addBillLog, FlowLog flowLog) {
        consumerBillRepository.save(consumerBill);
        if (CollectUtil.collectionNotEmpty(consumerBillChangeDetails)) {
            consumerBillChangeDetailRepository.batchInsert(consumerBillChangeDetails);
        }
        flowLogRepository.save(flowLog);
        addBillLogRepository.save(addBillLog);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addBatchSaveAll(List<ConsumerBill> consumerBills, List<ConsumerBillChangeDetail> consumerBillChangeDetails, List<AddBillLog> addBillLogs, List<FlowLog> flowLogs) {
        consumerBillRepository.batchInsert(consumerBills);
        if (CollectUtil.collectionNotEmpty(consumerBillChangeDetails)) {
            consumerBillChangeDetailRepository.batchInsert(consumerBillChangeDetails);
        }
        flowLogRepository.batchInsert(flowLogs);
        addBillLogRepository.batchInsert(addBillLogs);
    }
}
