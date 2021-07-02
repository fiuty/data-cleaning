package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.UtCoupon;
import com.chebianjie.datacleaning.domain.UtCouponUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author: matingting
 * @Date: 2021/07/02/16:36
 */
public interface UtCouponUserService {


    //车便捷
    public Page<UtCouponUser> pageCbjUtCouponUser(Pageable pageable);

    //车惠捷
    public Page<UtCouponUser> pageChjUtCouponUser(Pageable pageable);

    //车便捷
    public UtConsumer getCbjUtConsumerById(Long Uid);

    //车惠捷
    public UtConsumer getChjUtConsumerById(Long Uid);

    //用户中心
    public Consumer getConsumerByPhone(String phone);

    //用户中心
    public Consumer getConsumerByUnionid(String unionid);



}
