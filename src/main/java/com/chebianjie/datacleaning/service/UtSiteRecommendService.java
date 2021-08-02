package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtSiteRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UtSiteRecommendService {

    /**
     * 车便捷
     * @param pageable
     * @return
     */
    public Page<UtSiteRecommend> getCbjAllUtSiteRecommend(Pageable pageable);

    public void updateCbjUtSiteRecommendById(String consumerUnionAccount,Long id);

    public Page<UtSiteRecommend> getCbjCouponAllUtSiteRecommend(Pageable pageable);

    public void updateCbjCouponUtSiteRecommendById(String consumerUnionAccount,Long id);


    /**
     * 车惠捷
     * @param pageable
     * @return
     */
    public Page<UtSiteRecommend> getChjAllUtSiteRecommend(Pageable pageable);

    public void updateChjUtSiteRecommendById(String consumerUnionAccount,Long id);

    public Page<UtSiteRecommend> getChjCouponAllUtSiteRecommend(Pageable pageable);

    public void updateChjCouponUtSiteRecommendById(String consumerUnionAccount,Long id);


}
