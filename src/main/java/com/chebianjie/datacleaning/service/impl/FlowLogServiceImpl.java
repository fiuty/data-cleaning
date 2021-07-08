package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.FlowLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.FlowLogRepository;
import com.chebianjie.datacleaning.service.FlowLogService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
@Service
public class FlowLogServiceImpl implements FlowLogService {

    @Autowired
    private FlowLogRepository flowLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void save(Long utUserTotalFlowId, Platform platform, Integer status) {
        FlowLog flowLog = new FlowLog();
        flowLog.setUtUserTotalFlowId(utUserTotalFlowId);
        flowLog.setPlatform(platform);
        flowLog.setStatus(status);
        flowLogRepository.save(flowLog);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public FlowLog findByUtUserTotalFlowIdAndPlatform(Long utUserTotalFlowId, Platform platform) {
        return flowLogRepository.findByUtUserTotalFlowIdAndPlatform(utUserTotalFlowId, platform);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Boolean repeatClean(Long utUserTotalFlowId, Platform platform) {
        return flowLogRepository.findByUtUserTotalFlowIdAndPlatform(utUserTotalFlowId, platform) != null;
    }
}
