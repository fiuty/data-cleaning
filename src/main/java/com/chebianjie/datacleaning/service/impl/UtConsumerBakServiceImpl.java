package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumerBak;
import com.chebianjie.datacleaning.repository.UtConsumerBakRepository;
import com.chebianjie.datacleaning.service.UtConsumerBakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtConsumerBakServiceImpl implements UtConsumerBakService {

    @Autowired
    private UtConsumerBakRepository utConsumerBakRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void save(UtConsumerBak utConsumerBak) {
        utConsumerBakRepository.save(utConsumerBak);
    }
}
