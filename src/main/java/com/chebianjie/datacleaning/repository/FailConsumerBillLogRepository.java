package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.FailConsumerBillLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
public interface FailConsumerBillLogRepository extends JpaRepository<FailConsumerBillLog,Long>, JpaSpecificationExecutor<FailConsumerBillLog> {
    FailConsumerBillLog findByConsumerAccount(String consumerAccount);

}
