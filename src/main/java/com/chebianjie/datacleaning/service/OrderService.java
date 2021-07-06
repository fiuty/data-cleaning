package com.chebianjie.datacleaning.service;


/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2021-06-28
 */
public interface OrderService {




    void cleaningCBJWashOrder(Long consumerId, String phone, String consumerAccount);



    void cleaningCHJWashOrder(Long consumerId, String phone, String consumerAccount);

}
