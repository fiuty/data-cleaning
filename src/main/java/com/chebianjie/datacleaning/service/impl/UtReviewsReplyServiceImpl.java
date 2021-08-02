package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtReviewsReply;
import com.chebianjie.datacleaning.repository.UtReviewsReplyRepository;
import com.chebianjie.datacleaning.service.UtReviewsReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtReviewsReplyServiceImpl implements UtReviewsReplyService {

    @Autowired
    UtReviewsReplyRepository utReviewsReplyRepository;


    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtReviewsReply> getCbjAllUtReviewsReply(Pageable pageable) {
        return utReviewsReplyRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public void updateCbjUtReviewsReplyById(String consumerUnionAccount,Long id) {
        utReviewsReplyRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<UtReviewsReply> getCbjCouponAllUtReviewsReply(Pageable pageable) {
        return utReviewsReplyRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void updateCbjCouponUtReviewsReplyById(String consumerUnionAccount,Long id) {
        utReviewsReplyRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }






    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtReviewsReply> getChjAllUtReviewsReply(Pageable pageable) {
        return utReviewsReplyRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public void updateChjUtReviewsReplyById(String consumerUnionAccount, Long id) {
        utReviewsReplyRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<UtReviewsReply> getChjCouponAllUtReviewsReply(Pageable pageable) {
        return utReviewsReplyRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void updateChjCouponUtReviewsReplyById(String consumerUnionAccount, Long id) {
        utReviewsReplyRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


}
