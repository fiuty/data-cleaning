package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.ConsumerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerLogRepository extends JpaRepository<ConsumerLog, Long>, JpaSpecificationExecutor<ConsumerLog> {

    ConsumerLog findOneByUnionidAndStatusAndType(String unionid, int type, int status);

    ConsumerLog findOneByCbjAccountAndStatusAndType(String cbjAccount, int type, int status);

    ConsumerLog findOneByCbjIdAndStatusAndType(long cbjId, int type, int status);

    int countByUnionidAndTypeAndStatus(String unionid, int type, int status);

    int countByCbjAccountAndTypeAndStatus(String cbjAccount, int type, int status);
}
