package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring Data  repository for the UtConsumer entity.
 */
@Repository
@Transactional
public interface UtConsumerRepository extends JpaSpecificationExecutor<UtConsumer>, JpaRepository<UtConsumer, Long> {
}
