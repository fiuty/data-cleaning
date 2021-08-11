package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.FailConsumerBillLog;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
public interface FailConsumerBillLogService {

    void save(String consumerAccount);

    List<FailConsumerBillLog> findAll();
}
