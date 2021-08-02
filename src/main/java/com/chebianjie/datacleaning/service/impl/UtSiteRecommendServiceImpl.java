package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtSiteRecommend;
import com.chebianjie.datacleaning.repository.UtSiteRecommendRepository;
import com.chebianjie.datacleaning.service.UtSiteRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtSiteRecommendServiceImpl implements UtSiteRecommendService {



    @Autowired
    UtSiteRecommendRepository utSiteRecommendRepository;


    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtSiteRecommend> getCbjAllUtSiteRecommend(Pageable pageable) {
        return utSiteRecommendRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjUtSiteRecommendById(String consumerUnionAccount,Long id) {
        utSiteRecommendRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<UtSiteRecommend> getCbjCouponAllUtSiteRecommend(Pageable pageable) {
        return utSiteRecommendRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void updateCbjCouponUtSiteRecommendById(String consumerUnionAccount, Long id) {
            utSiteRecommendRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtSiteRecommend> getChjAllUtSiteRecommend(Pageable pageable) {
        return utSiteRecommendRepository.findAll(pageable);
    }


    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjUtSiteRecommendById(String consumerUnionAccount, Long id) {
        utSiteRecommendRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<UtSiteRecommend> getChjCouponAllUtSiteRecommend(Pageable pageable) {
        return utSiteRecommendRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void updateChjCouponUtSiteRecommendById(String consumerUnionAccount, Long id) {
        utSiteRecommendRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


}
