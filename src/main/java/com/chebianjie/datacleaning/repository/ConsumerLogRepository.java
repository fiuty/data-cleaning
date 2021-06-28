package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.ConsumerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerLogRepository extends JpaRepository<ConsumerLog, Long>, JpaSpecificationExecutor<ConsumerLog> {

    ConsumerLog findOneByUnionid(String unionid);

    ConsumerLog findOneByCbjAccount(String cbjAccount);
}
