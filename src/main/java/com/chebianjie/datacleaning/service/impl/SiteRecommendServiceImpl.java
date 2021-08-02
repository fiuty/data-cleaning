package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.SiteRecommend;
import com.chebianjie.datacleaning.repository.SiteRecommendRepository;
import com.chebianjie.datacleaning.service.SiteRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SiteRecommendServiceImpl implements SiteRecommendService {


    @Autowired
    SiteRecommendRepository siteRecommendRepository;

    @Override
    @DataSource(name = DataSourcesType.CBJ_AGENT)
    public Page<SiteRecommend> getCbjAllSiteRecommend(Pageable pageable) {
        return siteRecommendRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_AGENT)
    public void updateCbjSiteRecommendById(String consumerUnionAccount,Long id) {
        siteRecommendRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }






}
