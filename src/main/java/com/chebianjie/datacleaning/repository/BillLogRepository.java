package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.BillLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-01
 */
public interface BillLogRepository extends BaseRepository<BillLog,Long> {

    List<BillLog> findAllByStatus(Integer status);

    BillLog findByUnionAccountAndStatus(String unionAccount, int status);

    List<BillLog> findAllByUnionAccountIn(List<String> unionAccount);

}
