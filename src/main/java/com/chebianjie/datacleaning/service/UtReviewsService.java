package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UtReviewsService {

    /**
     *  车便捷
     */
    public Page<UtReviews> getCbjAllUtReviews(Pageable pageable);

    public void updateCbjUtReviewsById(String consumerUnionAccount,Long id);



    public Page<UtReviews> getCbjCouponAllUtReviews(Pageable pageable);

    public void updateCbjCouponUtReviewsById(String consumerUnionAccount,Long id);








    /**
     *  车惠捷
     */
    public Page<UtReviews> getChjAllUtReviews(Pageable pageable);

    public void updateChjUtReviewsById(String consumerUnionAccount,Long id);



    public Page<UtReviews> getChjCouponAllUtReviews(Pageable pageable);

    public void updateChjCouponUtReviewsById(String consumerUnionAccount,Long id);




















}
