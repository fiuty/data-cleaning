package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtChargeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
public interface UtChargeLogRepository extends JpaRepository<UtChargeLog,Long>, JpaSpecificationExecutor<UtChargeLog> {


    List<UtChargeLog> findAllByConsumerId(Long consumerId);


}
