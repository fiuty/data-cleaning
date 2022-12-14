package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerLogRepository extends JpaRepository<ConsumerLog, Long>, JpaSpecificationExecutor<ConsumerLog> {

    ConsumerLog findOneByUnionidAndStatusAndType(String unionid, int status, int type);

    ConsumerLog findOneByCbjAccountAndStatusAndType(String cbjAccount, int status, int type);

    ConsumerLog findOneByCbjIdAndStatusAndType(long cbjId, int status, int type);

    ConsumerLog findOneByChjIdAndStatusAndType(long chjId, int status, int type);

    ConsumerLog findOneByConsumerIdAndStatusAndType(long consumerId, int status, int type);

    int countByUnionidAndTypeAndStatus(String unionid, int type , int status);

    int countByCbjAccountAndTypeAndStatus(String cbjAccount, int type, int status);

    ConsumerLog findOneByChjAccountAndStatusAndType(String phone, int status, int type);

    ConsumerLog findOneByConsumerIdAndType(Long consumerId,Integer type);

    List<ConsumerLog> findAllByConsumerIdInAndType(List<Long> consumerIds, int type);

    List<ConsumerLog> findAllByCbjIdAndStatusAndType(Long cbjConsumerId,int status,int type);

    List<ConsumerLog> findAllByChjIdAndStatusAndType(Long chjConsumerId,int status,int type);


}
