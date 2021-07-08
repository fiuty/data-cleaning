package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
public interface UtUserTotalFlowRepository extends JpaRepository<UtUserTotalFlow,Long>, JpaSpecificationExecutor<UtUserTotalFlow> {

    List<UtUserTotalFlow> findAllByUid(Long cbjId);

    @Query(nativeQuery = true,value = "select * from ut_user_total_flow where create_time >= :timeFrom and create_time <= :timeTo order by id limit :pageNumber, :pageSize")
    List<UtUserTotalFlow> findAllByCreateTimeBetweenPage(@Param("timeFrom") Long timeFrom,@Param("timeTo") Long timeTo,@Param("pageNumber") int pageNumber,@Param("pageSize") int pageSize);

    int countByCreateTimeBetween(Long timeFrom, Long timeTo);
}
