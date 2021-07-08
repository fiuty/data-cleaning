package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.FlowLog;
import com.chebianjie.datacleaning.domain.enums.Platform;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface FlowLogService {

    void save(Long utUserTotalFlowId, Platform platform, Integer status);

    FlowLog findByUtUserTotalFlowIdAndPlatform(Long utUserTotalFlowId, Platform platform);

    Boolean repeatClean(Long utUserTotalFlowId, Platform platform);
}
