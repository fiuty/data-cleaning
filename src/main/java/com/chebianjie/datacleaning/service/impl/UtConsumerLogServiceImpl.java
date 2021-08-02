package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumerLog;
import com.chebianjie.datacleaning.repository.UtConsumerLogRepository;
import com.chebianjie.datacleaning.service.UtConsumerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtConsumerLogServiceImpl implements UtConsumerLogService {

    @Autowired
    UtConsumerLogRepository utConsumerLogRepository;

    @Override
    @DataSource(name = DataSourcesType.CBJ_REPORT)
    public Page<UtConsumerLog> getCbjAllUtConsumerLog(Pageable pageable) {
        return utConsumerLogRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_REPORT)
    public void updateCbjUtConsumerLog(String consumerUnionAccount, Long id) {
        utConsumerLogRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_REPORT)
    public Page<UtConsumerLog> getChjAllUtConsumerLog(Pageable pageable) {
        return utConsumerLogRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_REPORT)
    public void updateChjUtConsumerLog(String consumerUnionAccount, Long id) {
        utConsumerLogRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }
}
