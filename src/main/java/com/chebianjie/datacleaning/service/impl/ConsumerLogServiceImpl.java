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
    public ConsumerLog getOneByUnionId(String unionid) {
        return consumerLogRepository.findOneByUnionid(unionid);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByCbjAccount(String cbjAccount) {
        return consumerLogRepository.findOneByCbjAccount(cbjAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveOne(ConsumerLog consumerLog) {
        consumerLogRepository.save(consumerLog);
    }
}
