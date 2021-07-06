package com.chebianjie.datacleaning.repository.order;

import com.chebianjie.datacleaning.domain.order.DushOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2020-11-11
 */
public interface DushOrderRepository extends JpaSpecificationExecutor<DushOrder>, JpaRepository<DushOrder, Long>, CrudRepository<DushOrder, Long> {


    List<DushOrder> findAllByConsumerId(Long consumerId);


}
