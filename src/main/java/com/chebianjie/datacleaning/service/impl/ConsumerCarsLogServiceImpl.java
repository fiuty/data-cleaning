package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumerCarsLog;
import com.chebianjie.datacleaning.repository.ConsumerCarsLogRepository;
import com.chebianjie.datacleaning.service.ConsumerCarsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: matingting
 * @Date: 2021/06/30/18:09
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerCarsLogServiceImpl implements ConsumerCarsLogService {

    @Autowired
    ConsumerCarsLogRepository consumerCarsLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveOne(ConsumerCarsLog consumerCarsLog) {
        consumerCarsLogRepository.save(consumerCarsLog);
    }



}
