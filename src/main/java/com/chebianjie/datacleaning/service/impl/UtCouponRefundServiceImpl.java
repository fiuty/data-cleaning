package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtCouponRefund;
import com.chebianjie.datacleaning.repository.UtCouponRefundRepository;
import com.chebianjie.datacleaning.service.UtCouponRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtCouponRefundServiceImpl implements UtCouponRefundService {



    @Autowired
    UtCouponRefundRepository utCouponRefundRepository;


    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<UtCouponRefund> getCbjAllUtCouponRefund(Pageable pageable) {
        return utCouponRefundRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void updateCbjUtCouponRefundById(String consumerUnionAccount,Long id) {
        utCouponRefundRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }




    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<UtCouponRefund> getChjAllUtCouponRefund(Pageable pageable) {
        return utCouponRefundRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void updateChjUtCouponRefundById(String consumerUnionAccount, Long id) {
        utCouponRefundRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }






}
