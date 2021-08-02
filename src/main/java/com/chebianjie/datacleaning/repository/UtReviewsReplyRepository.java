package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtReviewsReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface UtReviewsReplyRepository extends JpaRepository<UtReviewsReply, Long>, JpaSpecificationExecutor<UtReviewsReply> {

    @Modifying
    @Query(value = "update UtReviewsReply set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    int updateConsumerUnionAccountById(@Param("consumerUnionAccount") String consumerUnionAccount, @Param("id") Long id);


}
