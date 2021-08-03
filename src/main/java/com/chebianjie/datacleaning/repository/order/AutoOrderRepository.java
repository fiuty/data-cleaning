package com.chebianjie.datacleaning.repository.order;


import com.chebianjie.datacleaning.domain.order.AutoOrder;
import com.chebianjie.datacleaning.domain.order.DushOrder;
import com.chebianjie.datacleaning.dto.ConsumerPhoneDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2020-11-12
 */
public interface AutoOrderRepository  extends JpaSpecificationExecutor<AutoOrder>, JpaRepository<AutoOrder, Long>, CrudRepository<AutoOrder, Long> {

    List<AutoOrder> findAllByConsumerId(Long consumerId);

    @Modifying
    @Query(value = "update AutoOrder set consumerAccount = :consumerAccount where consumerId =:consumerId")
    Integer updateConsumerAccount(@Param("consumerId") Long consumerId, @Param("consumerAccount") String consumerAccount);




    @Query(value = "SELECT new com.chebianjie.datacleaning.dto.ConsumerPhoneDTO(consumerId, tel) FROM AutoOrder where consumerId is not null and createTime >= :startTime group by consumerId")
    List<ConsumerPhoneDTO> findCountByStartTime(@Param("startTime") Long startTime);

    @Query(nativeQuery = true,value = "select * from auto_order  where consumer_id = :consumerId and create_time >= :startTime")
    List<AutoOrder> findByStartTimePage(@Param("startTime") Long startTime, @Param("consumerId") Long consumerId);

    @Modifying
    @Query(value = "update AutoOrder set consumerAccount = :consumerAccount where id =:id and createTime >= :startTime")
    Integer updateConsumerAccountByStartTime(@Param("id") Long id, @Param("consumerAccount") String consumerAccount, @Param("startTime") Long startTime);



}
