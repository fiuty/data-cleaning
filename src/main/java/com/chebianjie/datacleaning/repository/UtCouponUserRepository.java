package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtCouponUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UtCouponUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtCouponUserRepository extends JpaRepository<UtCouponUser, Long>, JpaSpecificationExecutor<UtCouponUser> {






}
