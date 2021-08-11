package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-08
 */
public interface ConsumerBillRepository extends JpaRepository<ConsumerBill, Long>, JpaSpecificationExecutor<ConsumerBill>, BaseRepository<ConsumerBill, Long> {

    List<ConsumerBill> findAllByUnionAccountIn(List<String> unionAccount);

    @Query(nativeQuery = true, value = "select * from consumer_bill where union_account = :unionAccount order by create_time desc limit 0,1")
    ConsumerBill findAllByUnionAccount(@Param("unionAccount") String unionAccount);

    List<ConsumerBill> findAllByUnionAccountAndCreateTimeGreaterThanOrderByCreateTimeDesc(String consumerAccount, LocalDateTime time);
}
