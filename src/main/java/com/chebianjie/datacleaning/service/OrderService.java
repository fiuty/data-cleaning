package com.chebianjie.datacleaning.service;


import com.chebianjie.datacleaning.dto.ConsumerPhoneDTO;

import java.util.List;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2021-06-28
 */
public interface OrderService {




    void cleaningCBJWashOrder(Integer consumerId, String phone, String consumerAccount);



    void cleaningCHJWashOrder(Integer consumerId, String phone, String consumerAccount);


    /**
     * 查询时间之后的订单数据
     * @param type  1.自助洗车  2.自助吸尘  3.全自动洗车 4.充值订单
     * @param startTime
     * @return
     */
    List<ConsumerPhoneDTO> cbjOrderTotal(Integer type, Long startTime);


    List findCBJOrderByPage(Integer type, Long startTime, Integer consumerId);


    void cbjUpdateOrder(Integer type, List<Object> dataList);



}
