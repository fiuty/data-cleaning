package com.chebianjie.datacleaning.service;


import com.chebianjie.datacleaning.domain.BillLog;

import java.util.List;

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
}
