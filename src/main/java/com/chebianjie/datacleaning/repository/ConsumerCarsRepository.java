package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.ConsumerCars;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConsumerCarsRepository extends JpaSpecificationExecutor<ConsumerCars>, JpaRepository<ConsumerCars, Long> {



}
