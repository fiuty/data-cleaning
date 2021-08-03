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


    List<UtChargeLog> findAllByConsumerId(Integer consumerId);


    @Modifying
    @Query(value = "update UtChargeLog set consumerAccount = :consumerAccount where id =:id")
    Integer updateConsumerAccount(@Param("id") Long id, @Param("consumerAccount") String consumerAccount);



    @Query(value = "SELECT new com.chebianjie.datacleaning.dto.ConsumerPhoneDTO(consumerId) FROM UtChargeLog where consumerId is not null and createTime >= :startTime group by consumerId")
    List<ConsumerPhoneDTO> findCountByStartTime(@Param("startTime") Long startTime);



    @Query(nativeQuery = true,value = "select * from UtChargeLog  where consumerId is not null and createTime >= :startTime ")
    List<UtChargeLog> findByStartTimePage(@Param("startTime") Long startTime);





}
