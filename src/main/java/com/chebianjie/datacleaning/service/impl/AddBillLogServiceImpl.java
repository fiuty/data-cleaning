package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.AddBillLogRepository;
import com.chebianjie.datacleaning.service.AddBillLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
@Service
public class AddBillLogServiceImpl implements AddBillLogService {

    @Autowired
    private AddBillLogRepository addBillLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Boolean repeatClean(Long utUserTotalFlowId, Platform platform) {
        return addBillLogRepository.findByUtUserTotalFlowIdAndPlatform(utUserTotalFlowId, platform) != null;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void save(Long utUserTotalFlowId, Platform platform, Integer status, String json) {
        AddBillLog addBillLog = new AddBillLog();
        addBillLog.setUtUserTotalFlowId(utUserTotalFlowId);
        addBillLog.setPlatform(platform);
        addBillLog.setStatus(status);
        addBillLog.setJson(json);
        addBillLogRepository.save(addBillLog);
    }
}
