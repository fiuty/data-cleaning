package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.repository.*;
import com.chebianjie.datacleaning.service.UtReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtReviewsServiceImpl implements UtReviewsService {

    @Autowired
    UtReviewsRepository utReviewsRepository;


    @Autowired
    ConsumerLogRepository consumerLogRepository;


    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtReviews> getCbjAllUtReviews(Pageable pageable) {
        return utReviewsRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjUtReviewsById(String consumerUnionAccount,Long id) {
        utReviewsRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<UtReviews> getCbjCouponAllUtReviews(Pageable pageable) {
        return utReviewsRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void updateCbjCouponUtReviewsById(String consumerUnionAccount, Long id) {
        utReviewsRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }






    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtReviews> getChjAllUtReviews(Pageable pageable) {
        return utReviewsRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjUtReviewsById(String consumerUnionAccount, Long id) {
        utReviewsRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<UtReviews> getChjCouponAllUtReviews(Pageable pageable) {
        return utReviewsRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void updateChjCouponUtReviewsById(String consumerUnionAccount, Long id) {
        utReviewsRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


}
