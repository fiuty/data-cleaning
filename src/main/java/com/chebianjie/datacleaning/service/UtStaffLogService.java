package com.chebianjie.datacleaning.service;

/**
 * @author zhengdayue
 * @date: 2021-07-09
 */
public interface UtStaffLogService {

    void updateCbjConsumerAccount(Long id, String consumerAccount);

    void updateChjConsumerAccount(Long id, String consumerAccount);

    void firstCbjClean();

    void firstChjClean();

}
