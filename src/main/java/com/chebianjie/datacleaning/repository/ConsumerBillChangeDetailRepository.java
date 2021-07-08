package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerBillChangeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * @author zhengdayue
 * @date: 2021-06-08
 */
public interface ConsumerBillChangeDetailRepository extends JpaRepository<ConsumerBillChangeDetail, Long>, JpaSpecificationExecutor<ConsumerBillChangeDetail> {

    void deleteAllByBillIdentifyIn(List<String> billIdentifies);

    List<ConsumerBillChangeDetail> findAllByBillIdentify(String billIdentify);
}
