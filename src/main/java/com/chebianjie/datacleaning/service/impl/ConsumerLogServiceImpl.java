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
    public ConsumerLog getOneByUnionId(String unionid, int type, int status) {
        return consumerLogRepository.findOneByUnionidAndStatusAndType(unionid, type, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByCbjAccount(String cbjAccount, int type, int status) {
        return consumerLogRepository.findOneByCbjAccountAndStatusAndType(cbjAccount, type, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveOne(ConsumerLog consumerLog) {
        consumerLogRepository.save(consumerLog);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public int countByUnionId(String unionid, int type, int status) {
        return consumerLogRepository.countByUnionidAndTypeAndStatus(unionid, type, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public int countByAccount(String cbjAccount, int type, int status) {
        return consumerLogRepository.countByCbjAccountAndTypeAndStatus(cbjAccount, type, status);
    }
}
