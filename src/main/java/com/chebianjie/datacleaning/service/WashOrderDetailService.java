package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.WashOrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WashOrderDetailService {



    public Page<WashOrderDetail> getCbjAllWashOrderDetail(Pageable pageable);

    public void updateCbjWashOrderDetail(String consumerUnionAccount,Long id);

    public Page<WashOrderDetail> getChjAllWashOrderDetail(Pageable pageable);

    public void updateChjWashOrderDetail(String consumerUnionAccount,Long id);







}
