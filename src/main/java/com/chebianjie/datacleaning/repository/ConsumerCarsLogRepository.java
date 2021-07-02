package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerCarsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author: matingting
 * @Date: 2021/06/30/18:06
 */
@Repository
public interface ConsumerCarsLogRepository extends JpaSpecificationExecutor<ConsumerCarsLog>, JpaRepository<ConsumerCarsLog, Long> {








}
