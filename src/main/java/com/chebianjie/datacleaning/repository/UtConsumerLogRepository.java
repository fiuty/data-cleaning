package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtConsumerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UtConsumerLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtConsumerLogRepository extends JpaRepository<UtConsumerLog, Long> {


    @Modifying
    @Query(value = "update UtConsumerLog set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount")String consumerUnionAccount, @Param("id")Long id);



}
