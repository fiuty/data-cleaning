package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtStaffLog;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-09
 */
public interface UtStaffLogService {

    void updateCbjConsumerAccount(Long id, String consumerAccount);

    void updateChjConsumerAccount(Long id, String consumerAccount);

    void firstCbjClean();

    void firstChjClean();

    void utstaffLogJob();

    int cbjCountByCreateTimeBetween(Long timeFrom, Long timeTo);

    int chjCountByCreateTimeBetween(Long timeFrom, Long timeTo);

    List<UtStaffLog> cbjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize);

    List<UtStaffLog> chjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize);

}
