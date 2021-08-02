package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtUserDriverLicens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UtUserDriverLicens entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtUserDriverLicensRepository extends JpaRepository<UtUserDriverLicens, Long> {


    @Modifying
    @Query(value = "update UtUserDriverLicens set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount") String consumerUnionAccount, @Param("id") Long id);






}
