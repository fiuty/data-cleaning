package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerLogServiceImpl implements ConsumerLogService {

    @Autowired
    private ConsumerLogRepository consumerLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByUnionId(String unionid, int status) {
        return consumerLogRepository.findOneByUnionidAndStatus(unionid, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByCbjAccount(String cbjAccount, int status) {
        return consumerLogRepository.findOneByCbjAccountAndStatus(cbjAccount, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveOne(ConsumerLog consumerLog) {
        consumerLogRepository.save(consumerLog);
    }
}
