package com.chebianjie.datacleaning.service;


import com.chebianjie.datacleaning.domain.BillLog;

import java.util.List;
import java.util.Map;

/**
 * @author zhengdayue
 * @date: 2021-07-01
 */
public interface BillLogService {

    void save(String unionAccount, Integer status);

    List<BillLog> findByStatus(Integer status);

    /**
     * 是否重复清洗
     */
    Boolean repeatClean(String unionAccount);

    void deleteAll(List<BillLog> billLogs);

    void saveAll(List<String> unionAccounts, Integer status);

    Map<String, Boolean> batchRepeatClean(List<String> unionAccount);
}
