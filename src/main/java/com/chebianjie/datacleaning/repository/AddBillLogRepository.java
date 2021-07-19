package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface AddBillLogRepository extends JpaRepository<AddBillLog, Long>, JpaSpecificationExecutor<AddBillLog>, BaseRepository<AddBillLog, Long> {

    AddBillLog findByUtUserTotalFlowIdAndPlatform(Long utUserTotalFlowId, Platform platform);

}
