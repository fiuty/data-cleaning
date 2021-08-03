package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.DataSynTime;


/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
public interface DataSynTimeService {

    void updateDataSynTime(DataSynTime dataSynTime);

    DataSynTime findBySynType(Integer type);
}
