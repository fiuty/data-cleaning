package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.SiteRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SiteRecommendService {


    public Page<SiteRecommend> getCbjAllSiteRecommend(Pageable pageable);

    public void updateCbjSiteRecommendById(String consumerUnionAccount,Long id);




}
