package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
/**
 * @author zhengdayue
 * @date: 2021-06-28
 */
public interface ConsumerBillService {

    void clean(int pageNumber, int pageSize);

    void threadClean(Consumer consumer);
}
