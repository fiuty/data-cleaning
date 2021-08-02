package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface UtFeedbackRepository extends JpaRepository<UtFeedback, Long>, JpaSpecificationExecutor<UtFeedback> {

    @Modifying
    @Query(value = "update UtFeedback set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount") String consumerUnionAccount, @Param("id") Long id);

}
