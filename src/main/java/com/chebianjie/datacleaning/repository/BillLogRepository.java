package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.BillLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-01
 */
public interface BillLogRepository extends JpaRepository<BillLog, Long>, JpaSpecificationExecutor<BillLog>  {

    List<BillLog> findAllByStatus(Integer status);

}
