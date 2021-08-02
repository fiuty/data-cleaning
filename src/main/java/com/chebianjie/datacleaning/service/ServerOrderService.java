package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ServerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServerOrderService {


    /**
     *  车便捷
     */
    public Page<ServerOrder> getCbjAllServerOrder(Pageable pageable);

    public void updateCbjServerOrderById(String consumerUnionAccount,Long id);












}
