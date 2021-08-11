package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.enums.Platform;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface AddBillLogService {

    Boolean repeatClean(Long utUserTotalFlowId, Platform platform);

    void save(Long utUserTotalFlowId, Platform platform, Integer status, String json);

    List<AddBillLog> findAllByPage(int pageNumber, int pageSize);

    int countByIdLessThanEqual(Long id);
}
