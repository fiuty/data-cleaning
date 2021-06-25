package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhengdayue
 * @date: 2021-06-25
 */
@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private UtConsumerRepository utConsumerRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer listMster() {
        UtConsumer utConsumer = utConsumerRepository.findById(10L).orElse(null);
        log.info("utConsumer:{}",utConsumer);
        return null;
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer listSlave() {
        UtConsumer utConsumer = utConsumerRepository.findById(131086L).orElse(null);
        log.info("utConsumer:{}",utConsumer);
        return null;
    }
}
