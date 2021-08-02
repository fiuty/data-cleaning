package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtReviewsRepository extends JpaRepository<UtReviews, Long>, JpaSpecificationExecutor<UtReviews> {

    @Modifying
    @Query(value = "update UtReviews r set r.consumerUnionAccount = :consumerUnionAccount where  r.id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount")String consumerUnionAccount,@Param("id")Long id);








}
