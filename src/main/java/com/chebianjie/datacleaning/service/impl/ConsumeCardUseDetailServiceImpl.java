package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumeCardUseDetail;
import com.chebianjie.datacleaning.repository.ConsumeCardUseDetailRepository;
import com.chebianjie.datacleaning.service.ConsumeCardUseDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumeCardUseDetailServiceImpl implements ConsumeCardUseDetailService {


    @Autowired
    ConsumeCardUseDetailRepository consumeCardUseDetailRepository;



    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public Page<ConsumeCardUseDetail> getCbjAllConsumeCardUseDetail(Pageable pageable) {
        return consumeCardUseDetailRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_COUPON)
    public void updateCbjConsumeCardUseDetailById(String consumerUnionAccount,Long id) {
        consumeCardUseDetailRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public Page<ConsumeCardUseDetail> getChjAllConsumeCardUseDetail(Pageable pageable) {
        return consumeCardUseDetailRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_COUPON)
    public void updateChjConsumeCardUseDetailById(String consumerUnionAccount, Long id) {
        consumeCardUseDetailRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


}
