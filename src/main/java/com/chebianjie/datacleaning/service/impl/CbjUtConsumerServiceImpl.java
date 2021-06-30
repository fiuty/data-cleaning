package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CbjUtConsumerServiceImpl implements CbjUtConsumerService {

    @Autowired
    private UtConsumerRepository utConsumerRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtConsumer> pageUtConsumer(Pageable pageable) {
        return utConsumerRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer getUtConsumerById(Long id) {
        return utConsumerRepository.findById(id).orElse(null);
    }
}
