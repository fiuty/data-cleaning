package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.DataSynTime;
import com.chebianjie.datacleaning.repository.DataSynTimeRepository;
import com.chebianjie.datacleaning.service.DataSynTimeService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Service
public class DataSynTimeServiceImpl implements DataSynTimeService {

    @Autowired
    private DataSynTimeRepository dataSynTimeRepository;

    @Override
    @RabbitHandler
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void updateDataSynTime(DataSynTime dataSynTime) {
        dataSynTimeRepository.save(dataSynTime);
    }
}
