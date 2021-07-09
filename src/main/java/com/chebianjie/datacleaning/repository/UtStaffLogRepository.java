package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtStaffLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author zhengdayue
 * @date: 2021-07-09
 */
public interface UtStaffLogRepository extends JpaRepository<UtStaffLog, Long>, JpaSpecificationExecutor<UtStaffLog> {

    @Modifying
    @Query(value = "update UtStaffLog set consumerUnionAccount = :consumerAccount where  id = :id")
    void updateConsumerAccount(@Param("id") Long id,@Param("consumerAccount") String consumerAccount);

    int countByCreatetimeLessThanEqual(Long creatTime);

    @Query(nativeQuery = true, value = "select * from ut_staff_log where create_time <= :createTime order by create_time asc limit :pageNumber, :pageSize")
    List<UtStaffLog> findAllByCreateTimePage(@Param("createTime") Long createTime, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);
}
