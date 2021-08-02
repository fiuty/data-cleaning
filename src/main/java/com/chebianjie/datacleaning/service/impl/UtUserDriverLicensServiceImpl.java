package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtUserDriverLicens;
import com.chebianjie.datacleaning.repository.UtUserDriverLicensRepository;
import com.chebianjie.datacleaning.service.UtUserDriverLicensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtUserDriverLicensServiceImpl implements UtUserDriverLicensService {

    @Autowired
    UtUserDriverLicensRepository utUserDriverLicensRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtUserDriverLicens> getCbjAllUtUserDriverLicens(Pageable pageable) {
        return utUserDriverLicensRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjUtUserDriverLicensById(String consumerUnionAccount, Long id) {
        utUserDriverLicensRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtUserDriverLicens> getChjAllUtUserDriverLicens(Pageable pageable) {
        return utUserDriverLicensRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjUtUserDriverLicensById(String consumerUnionAccount, Long id) {
        utUserDriverLicensRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }





}
