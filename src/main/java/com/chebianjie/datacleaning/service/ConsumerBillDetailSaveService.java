package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerBillChangeDetail;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface ConsumerBillDetailSaveService {

    void save(ConsumerBillChangeDetail consumerBillChangeDetail);

    List<ConsumerBillChangeDetail> findByBillIdentify(String billIdentify);
}
