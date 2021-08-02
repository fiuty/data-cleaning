package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtCouponRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




public interface UtCouponRefundRepository extends JpaRepository<UtCouponRefund, Long>, JpaSpecificationExecutor<UtCouponRefund> {

    @Modifying
    @Query(value = "update UtCouponRefund set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount")String consumerUnionAccount, @Param("id")Long id);

}
