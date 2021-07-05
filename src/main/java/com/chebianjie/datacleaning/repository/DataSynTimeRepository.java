package com.chebianjie.datacleaning.repository;

import com.chebianjie.datacleaning.domain.DataSynTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
public interface DataSynTimeRepository extends JpaRepository<DataSynTime,Long>, JpaSpecificationExecutor<DataSynTime> {
    DataSynTime findBySynType(int synType);
}
