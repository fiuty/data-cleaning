package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumerBalanceClearLog;
import com.chebianjie.datacleaning.repository.UtConsumerBalanceClearLogRepository;
import com.chebianjie.datacleaning.service.UtConsumerBalanceClearLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtConsumerBalanceClearLogServiceImpl implements UtConsumerBalanceClearLogService {

    @Autowired
    UtConsumerBalanceClearLogRepository utConsumerBalanceClearLogRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtConsumerBalanceClearLog> getCbjAllUtConsumerBalanceClearLog(Pageable pageable) {
        return utConsumerBalanceClearLogRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjUtConsumerBalanceClearLogById(String consumerUnionAccount, Long id) {
        utConsumerBalanceClearLogRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtConsumerBalanceClearLog> getChjAllUtConsumerBalanceClearLog(Pageable pageable) {
        return utConsumerBalanceClearLogRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjUtConsumerBalanceClearLogById(String consumerUnionAccount, Long id) {
        utConsumerBalanceClearLogRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


}
