package com.chebianjie.datacleaning.repository;
import com.chebianjie.datacleaning.domain.UtSiteRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UtSiteRecommendRepository extends JpaRepository<UtSiteRecommend, Long>, JpaSpecificationExecutor<UtSiteRecommend> {

    @Modifying
    @Query(value = "update UtSiteRecommend set consumerUnionAccount = :consumerUnionAccount where  id = :id")
    void updateConsumerUnionAccountById(@Param("consumerUnionAccount") String consumerUnionAccount, @Param("id") Long id);



}
