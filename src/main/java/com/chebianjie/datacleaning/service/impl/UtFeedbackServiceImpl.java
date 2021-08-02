package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtFeedback;
import com.chebianjie.datacleaning.repository.UtFeedbackRepository;
import com.chebianjie.datacleaning.service.UtFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtFeedbackServiceImpl implements UtFeedbackService {

    @Autowired
    UtFeedbackRepository utFeedbackRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtFeedback> getCbjAllUtFeedback(Pageable pageable) {
        return utFeedbackRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjUtFeedbackById(String consumerUnionAccount,Long id) {
        utFeedbackRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<UtFeedback> getCbjCouponAllUtFeedback(Pageable pageable) {
        return utFeedbackRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void updateCbjCouponUtFeedbackById(String consumerUnionAccount, Long id) {
        utFeedbackRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }



    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtFeedback> getChjAllUtFeedback(Pageable pageable) {
        return utFeedbackRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjUtFeedbackById(String consumerUnionAccount, Long id) {
        utFeedbackRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<UtFeedback> getChjCouponAllUtFeedback(Pageable pageable) {
        return utFeedbackRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void updateChjCouponUtFeedbackById(String consumerUnionAccount, Long id) {

    }



}
