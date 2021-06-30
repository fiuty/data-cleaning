package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtChargeLog;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
public interface ChargeLogService {

    UtChargeLog cbjFindById(Long id);

    UtChargeLog chjFindById(Long id);

}
