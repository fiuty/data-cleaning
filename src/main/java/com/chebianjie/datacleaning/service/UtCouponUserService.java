package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtCouponUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author: matingting
 * @Date: 2021/07/02/16:36
 */
public interface UtCouponUserService {

    UtCouponUser getCbjOneById(long id);

    UtCouponUser getChjOneById(long id);

    //车便捷
    Page<UtCouponUser> pageCbjUtCouponUser(Pageable pageable);

    //车惠捷
    Page<UtCouponUser> pageChjUtCouponUser(Pageable pageable);

    //车便捷
    void mergeCbjConsumerCoupon(UtCouponUser utCouponUser, Consumer consumer) throws Exception;

    //车惠捷
    void mergeChjConsumerCoupon(UtCouponUser utCouponUser, Consumer consumer) throws Exception;
}
