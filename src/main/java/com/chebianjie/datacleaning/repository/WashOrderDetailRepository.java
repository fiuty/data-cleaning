package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.WashOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface WashOrderDetailRepository extends JpaRepository<WashOrderDetail, Long> {

    @Modifying
    @Query(value = "update WashOrderDetail set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount") String consumerUnionAccount, @Param("id") Long id);






}
