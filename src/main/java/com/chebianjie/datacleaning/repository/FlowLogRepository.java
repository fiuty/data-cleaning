package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.FlowLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface FlowLogRepository extends JpaRepository<FlowLog, Long>, JpaSpecificationExecutor<FlowLog> {

    FlowLog findByUtUserTotalFlowIdAndPlatform(Long utUserTotalFlowId, Platform platform);
}
