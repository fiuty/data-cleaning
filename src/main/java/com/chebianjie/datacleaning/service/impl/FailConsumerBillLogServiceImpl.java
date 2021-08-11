package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.FailConsumerBillLog;
import com.chebianjie.datacleaning.repository.FailConsumerBillLogRepository;
import com.chebianjie.datacleaning.service.FailConsumerBillLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class FailConsumerBillLogServiceImpl implements FailConsumerBillLogService {

    @Autowired
    private FailConsumerBillLogRepository failConsumerBillLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void save(String consumerAccount) {
        FailConsumerBillLog failConsumerBillLog = failConsumerBillLogRepository.findByConsumerAccount(consumerAccount);
        if (failConsumerBillLog == null) {
            failConsumerBillLog = new FailConsumerBillLog();
            failConsumerBillLog.setConsumerAccount(consumerAccount);
            failConsumerBillLogRepository.save(failConsumerBillLog);
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<FailConsumerBillLog> findAll() {
        return failConsumerBillLogRepository.findAll();
    }
}
