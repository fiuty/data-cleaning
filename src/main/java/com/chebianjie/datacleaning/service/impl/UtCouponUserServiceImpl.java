package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtCouponUser;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import com.chebianjie.datacleaning.repository.ConsumerRepository;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.repository.UtCouponUserRepository;
import com.chebianjie.datacleaning.service.UtCouponUserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UtCouponUserServiceImpl implements UtCouponUserService {

    @Autowired
    UtCouponUserRepository utCouponUserRepository;

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public UtCouponUser getCbjOneById(long id) {
        return utCouponUserRepository.findById(id).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public UtCouponUser getChjOneById(long id) {
        return utCouponUserRepository.findById(id).orElse(null);
    }

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
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void mergeCbjConsumerCoupon(UtCouponUser utCouponUser, Consumer consumer) throws Exception {
        try {
            utCouponUser.setConsumerUnionAccount(consumer.getUnionAccount());
            utCouponUserRepository.save(utCouponUser);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void mergeChjConsumerCoupon(UtCouponUser utCouponUser, Consumer consumer) throws Exception {
        try {
            utCouponUser.setConsumerUnionAccount(consumer.getUnionAccount());
            utCouponUserRepository.save(utCouponUser);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
