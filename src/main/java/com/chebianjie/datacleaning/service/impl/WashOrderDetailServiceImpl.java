package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.WashOrderDetail;
import com.chebianjie.datacleaning.repository.WashOrderDetailRepository;
import com.chebianjie.datacleaning.service.WashOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class WashOrderDetailServiceImpl implements WashOrderDetailService {

    @Autowired
    WashOrderDetailRepository washOrderDetailRepository;

    @Override
    @DataSource(name = DataSourcesType.CBJ_REPORT)
    public Page<WashOrderDetail> getCbjAllWashOrderDetail(Pageable pageable) {
        return washOrderDetailRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_REPORT)
    public void updateCbjWashOrderDetail(String consumerUnionAccount, Long id) {
        washOrderDetailRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_REPORT)
    public Page<WashOrderDetail> getChjAllWashOrderDetail(Pageable pageable) {
        return washOrderDetailRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_REPORT)
    public void updateChjWashOrderDetail(String consumerUnionAccount, Long id) {
        washOrderDetailRepository.updateConsumerUnionAccountById(consumerUnionAccount, id);
    }



}
