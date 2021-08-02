package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.StaffInviteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffInviteDetailRepository extends JpaRepository<StaffInviteDetail, Long>, JpaSpecificationExecutor<StaffInviteDetail> {

    @Modifying
    @Query(value = "update StaffInviteDetail set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount")String consumerUnionAccount, @Param("id")Long id);




}
