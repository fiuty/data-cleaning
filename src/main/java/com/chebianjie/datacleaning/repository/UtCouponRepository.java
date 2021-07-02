package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UtCoupon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtCouponRepository extends JpaRepository<UtCoupon, Long>, JpaSpecificationExecutor<UtCoupon> {



}
