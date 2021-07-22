package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 消费者 Repository
 *
 * @author 许泽坤
 * @create 2021-06-03 14:18
 */
public interface ConsumerRepository extends JpaRepository<Consumer, Long>, JpaSpecificationExecutor<Consumer> {

    Consumer findByWechatUnionId(String unionid);

    Consumer findByPhone(String phone);

    int countByRegistryTimeLessThanEqual(LocalDateTime flowConsumerTime);

    @Query(nativeQuery = true,value = "select * from consumer t,(select id from consumer order by id limit :pageNumber, :pageSize) temp where t.id = temp.id")
    List<Consumer> findAllByPage(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);


    @Query(nativeQuery = true,value = "select count(1) from consumer")
    Long findTotalConsumer();

    Consumer findByUnionAccount(String unionAccount);

    List<Consumer> findAllByUnionAccountIn(List<String> unionAccounts);

}
