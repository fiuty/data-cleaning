package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.DataSynTime;
import com.chebianjie.datacleaning.repository.DataSynTimeRepository;
import com.chebianjie.datacleaning.service.DataSynTimeService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class DataSynTimeServiceImpl implements DataSynTimeService {

    @Autowired
    private DataSynTimeRepository dataSynTimeRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void updateDataSynTime(DataSynTime dataSynTime) {
        dataSynTimeRepository.save(dataSynTime);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public DataSynTime findBySynType(Integer type) {
        return dataSynTimeRepository.findBySynType(type);
    }
}
