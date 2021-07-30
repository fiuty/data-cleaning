package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    int countByCreatetimeLessThanEqual(Long consumerTime);

    @Query(nativeQuery = true,value = "select * from ut_consumer t,(select id from ut_consumer order by id limit :pageNumber, :pageSize) temp where t.id = temp.id")
    List<UtConsumer> findAllByPage(@Param("pageNumber") int pageNumber,@Param("pageSize") int pageSize);

    int countByUnionidAndStatue(String unionid, int statue);
}
