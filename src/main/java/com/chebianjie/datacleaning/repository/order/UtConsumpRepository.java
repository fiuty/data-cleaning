package com.chebianjie.datacleaning.repository.order;


import com.chebianjie.datacleaning.domain.order.UtConsump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UtConsump entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtConsumpRepository extends JpaSpecificationExecutor<UtConsump>, JpaRepository<UtConsump, Long>, CrudRepository<UtConsump, Long> {


    List<UtConsump> findAllByConsumerId(Integer consumerId);


}
