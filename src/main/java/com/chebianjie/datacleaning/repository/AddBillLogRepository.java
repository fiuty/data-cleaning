package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.AddBillLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
public interface AddBillLogRepository extends JpaRepository<AddBillLog, Long>, JpaSpecificationExecutor<AddBillLog>, BaseRepository<AddBillLog, Long> {

    AddBillLog findByUtUserTotalFlowIdAndPlatform(Long utUserTotalFlowId, Platform platform);

    @Query(nativeQuery = true,value = "select * from add_bill_log t,(select id from add_bill_log order by id limit :pageNumber, :pageSize) temp where t.id = temp.id")
    List<AddBillLog> findAllByPage(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);

    int countByIdLessThanEqual(Long id);
}
