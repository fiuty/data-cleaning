package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data  repository for the UtConsumer entity.
 */
@Repository
public interface UtConsumerRepository extends JpaSpecificationExecutor<UtConsumer>, JpaRepository<UtConsumer, Long> {

    UtConsumer findByUnionid(String unionid);

    UtConsumer findByAccount(String account);

    List<UtConsumer> findListByAccount(String account);

    List<UtConsumer> findListByUnionid(String unionid);
}
