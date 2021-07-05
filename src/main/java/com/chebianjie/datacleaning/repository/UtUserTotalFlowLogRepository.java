package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtUserTotalFlowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
public interface UtUserTotalFlowLogRepository extends JpaRepository<UtUserTotalFlowLog,Long>, JpaSpecificationExecutor<UtUserTotalFlowLog> {
}
