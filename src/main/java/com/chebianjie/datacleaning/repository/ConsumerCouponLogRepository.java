package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerCouponLog;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerCouponLogRepository extends JpaRepository<ConsumerCouponLog, Long>, JpaSpecificationExecutor<ConsumerCouponLog> {

    ConsumerCouponLog findOneByUtCouponUserIdAndStatusAndType(long utCouponUserId, int status, int type);
}
