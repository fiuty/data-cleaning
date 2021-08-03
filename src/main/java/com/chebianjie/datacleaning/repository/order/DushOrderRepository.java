package com.chebianjie.datacleaning.repository.order;

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
 * @Date: 2020-11-11
 */
public interface DushOrderRepository extends JpaSpecificationExecutor<DushOrder>, JpaRepository<DushOrder, Long>, CrudRepository<DushOrder, Long> {


    List<DushOrder> findAllByConsumerId(Integer consumerId);

    @Modifying
    @Query(value = "update DushOrder set consumerAccount = :consumerAccount where id =:id")
    Integer updateConsumerAccount(@Param("id") Long id, @Param("consumerAccount") String consumerAccount);



    @Query(value = "SELECT new com.chebianjie.datacleaning.dto.ConsumerPhoneDTO(consumerId, tel) FROM DushOrder where consumerId is not null and createTime >= :startTime group by consumerId")
    List<ConsumerPhoneDTO> findCountByStartTime(@Param("startTime") Long startTime);


    @Query(nativeQuery = true,value = "select * from DushOrder  where consumerId is not null and createTime >= :startTime ")
    List<DushOrder> findByStartTimePage(@Param("startTime") Long startTime);



}
