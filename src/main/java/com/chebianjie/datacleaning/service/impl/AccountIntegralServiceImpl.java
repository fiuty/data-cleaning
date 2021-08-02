package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.AccountIntegral;
import com.chebianjie.datacleaning.repository.AccountIntegralRepository;
import com.chebianjie.datacleaning.service.AccountIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountIntegralServiceImpl implements AccountIntegralService {

    @Autowired
    AccountIntegralRepository accountIntegralRepository;


    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<AccountIntegral> getCbjAllAccountIntegral(Pageable pageable) {
        return accountIntegralRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjAccountIntegralById(String consumerUnionAccount, Long id) {
        accountIntegralRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<AccountIntegral> getChjAllAccountIntegral(Pageable pageable) {
        return accountIntegralRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjAccountIntegralById(String consumerUnionAccount, Long id) {
        accountIntegralRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }
}
