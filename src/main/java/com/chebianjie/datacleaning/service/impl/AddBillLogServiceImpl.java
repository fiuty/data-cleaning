package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.AddBillLogRepository;
import com.chebianjie.datacleaning.service.AddBillLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<AddBillLog> findAllByPage(int pageNumber, int pageSize) {
        return addBillLogRepository.findAllByPage(pageNumber, pageSize);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public int countByIdLessThanEqual(Long id) {
        return addBillLogRepository.countByIdLessThanEqual(id);
    }

}
