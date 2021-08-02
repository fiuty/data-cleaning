package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ServerOrder;
import com.chebianjie.datacleaning.repository.ServerOrderRepository;
import com.chebianjie.datacleaning.service.ServerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ServerOrderServiceImpl implements ServerOrderService {

    @Autowired
    ServerOrderRepository serverOrderRepository;



    @Override
    @DataSource(name = DataSourcesType.CBJ_CAR_SERVER)
    public Page<ServerOrder> getCbjAllServerOrder(Pageable pageable) {
        return serverOrderRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_CAR_SERVER)
    public void updateCbjServerOrderById(String consumerUnionAccount,Long id) {
        serverOrderRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }










}
