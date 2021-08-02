package com.chebianjie.datacleaning.service;


import com.chebianjie.datacleaning.domain.UtCouponRefund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UtCouponRefundService {


    public Page<UtCouponRefund> getCbjAllUtCouponRefund(Pageable pageable);

    public void updateCbjUtCouponRefundById(String consumerUnionAccount,Long id);

    public Page<UtCouponRefund> getChjAllUtCouponRefund(Pageable pageable);

    public void updateChjUtCouponRefundById(String consumerUnionAccount,Long id);







}
