package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengdayue
 * @date: 2021-06-08
 */
public interface ConsumerBillRepository extends JpaRepository<ConsumerBill, Long>, JpaSpecificationExecutor<ConsumerBill> {

}
