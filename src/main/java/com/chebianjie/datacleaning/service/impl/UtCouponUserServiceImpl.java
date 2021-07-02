package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.UtCoupon;
import com.chebianjie.datacleaning.domain.UtCouponUser;
import com.chebianjie.datacleaning.repository.*;
import com.chebianjie.datacleaning.service.UtCouponUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: matingting
 * @Date: 2021/07/02/16:38
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtCouponUserServiceImpl implements UtCouponUserService {

    @Autowired
    UtCouponUserRepository utCouponUserRepository;

    @Autowired
    ConsumerLogRepository consumerLogRepository;

    @Autowired
    UtConsumerRepository utConsumerRepository;

    @Autowired
    ConsumerRepository consumerRepository;


    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<UtCouponUser> pageCbjUtCouponUser(Pageable pageable) {
        return utCouponUserRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<UtCouponUser> pageChjUtCouponUser(Pageable pageable) {
        return utCouponUserRepository.findAll(pageable);
    }


    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer getCbjUtConsumerById(Long Uid) {
        return utConsumerRepository.findById(Uid).orElse(null);
    }


    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer getChjUtConsumerById(Long Uid) {
        return utConsumerRepository.findById(Uid).orElse(null);
    }


    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer getConsumerByPhone(String phone) {
        return consumerRepository.findByPhone(phone);
    }


    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer getConsumerByUnionid(String unionid) {
        return consumerRepository.findByWechatUnionId(unionid);
    }



}
