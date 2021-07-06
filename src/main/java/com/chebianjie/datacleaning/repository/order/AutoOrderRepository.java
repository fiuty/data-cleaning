package com.chebianjie.datacleaning.repository.order;


import com.chebianjie.datacleaning.domain.order.AutoOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2020-11-12
 */
public interface AutoOrderRepository  extends JpaSpecificationExecutor<AutoOrder>, JpaRepository<AutoOrder, Long>, CrudRepository<AutoOrder, Long> {

    List<AutoOrder> findAllByConsumerId(Long consumerId);

}
