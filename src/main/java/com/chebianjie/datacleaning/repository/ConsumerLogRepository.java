package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.ConsumerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerLogRepository extends JpaRepository<ConsumerLog, Long>, JpaSpecificationExecutor<ConsumerLog> {

    ConsumerLog findOneByUnionidAndStatus(String unionid, int status);

    ConsumerLog findOneByCbjAccountAndStatus(String cbjAccount, int status);

    ConsumerLog findOneByCbjIdAndStatus(long cbjId, int status);
}
