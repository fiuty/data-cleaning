package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtConsumerBak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UtConsumer entity.
 */
@Repository
public interface UtConsumerBakRepository extends JpaSpecificationExecutor<UtConsumerBak>, JpaRepository<UtConsumerBak, Long> {

}
