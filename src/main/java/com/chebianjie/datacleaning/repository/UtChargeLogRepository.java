package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.UtChargeLog;
import com.chebianjie.datacleaning.dto.ConsumerPhoneDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
public interface UtChargeLogRepository extends JpaRepository<UtChargeLog,Long>, JpaSpecificationExecutor<UtChargeLog> {


    List<UtChargeLog> findAllByConsumerId(Long consumerId);


    @Modifying
    @Query(value = "update UtChargeLog set consumerAccount = :consumerAccount where consumerId =:consumerId")
    Integer updateConsumerAccount(@Param("consumerId") Long consumerId, @Param("consumerAccount") String consumerAccount);



    @Query(value = "SELECT new com.chebianjie.datacleaning.dto.ConsumerPhoneDTO(consumerId) FROM UtChargeLog where consumerId is not null and createTime >= :startTime group by consumerId")
    List<ConsumerPhoneDTO> findCountByStartTime(@Param("startTime") Long startTime);



    @Query(nativeQuery = true,value = "select * from ut_charge_log  where consumer_id = :consumerId and create_time >= :startTime")
    List<UtChargeLog> findByStartTimePage(@Param("startTime") Long startTime, @Param("consumerId") Long consumerId);


    @Modifying
    @Query(value = "update UtChargeLog set consumerAccount = :consumerAccount where consumerId =:consumerId and createTime >= :startTime")
    Integer updateConsumerAccountByStartTime(@Param("consumerId") Long consumerId, @Param("consumerAccount") String consumerAccount, @Param("startTime") Long startTime);



}
