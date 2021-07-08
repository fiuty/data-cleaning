package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.enums.Platform;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface AddBillLogService {

    Boolean repeatClean(Long utUserTotalFlowId, Platform platform);

    void save(Long utUserTotalFlowId, Platform platform, Integer status, String json);
}
