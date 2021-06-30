package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtChargeLog;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
public interface UtUserTotalFlowRepository extends JpaRepository<UtUserTotalFlow,Long>, JpaSpecificationExecutor<UtUserTotalFlow> {

    List<UtUserTotalFlow> findAllByUid(Long cbjId);
}
