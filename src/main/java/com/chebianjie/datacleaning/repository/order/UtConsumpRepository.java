package com.chebianjie.datacleaning.repository.order;


import com.chebianjie.datacleaning.domain.order.UtConsump;
import com.chebianjie.datacleaning.dto.ConsumerPhoneDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UtConsump entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtConsumpRepository extends JpaSpecificationExecutor<UtConsump>, JpaRepository<UtConsump, Long>, CrudRepository<UtConsump, Long> {


    List<UtConsump> findAllByConsumerId(Long consumerId);

    @Modifying
    @Query(value = "update UtConsump set consumerAccount = :consumerAccount where id =:id")
    Integer updateConsumerAccount(@Param("id") Long id, @Param("consumerAccount") String consumerAccount);



    @Query(value = "SELECT new com.chebianjie.datacleaning.dto.ConsumerPhoneDTO(consumerId, tel) FROM UtConsump where consumerId is not null and createTime >= :startTime group by consumerId")
    List<ConsumerPhoneDTO> findCountByStartTime(@Param("startTime") Long startTime);



    @Query(nativeQuery = true,value = "select * from ut_consump  where consumer_id = :consumerId and create_time >= :startTime ")
    List<UtConsump> findByStartTimePage(@Param("startTime") Long startTime, @Param("consumerId") Long consumerId);


    @Modifying
    @Query(value = "update UtConsump set consumerAccount = :consumerAccount where id =:id and createTime >= :startTime")
    Integer updateConsumerAccountByStartTime(@Param("id") Long id, @Param("consumerAccount") String consumerAccount, @Param("startTime") Long startTime);



}
