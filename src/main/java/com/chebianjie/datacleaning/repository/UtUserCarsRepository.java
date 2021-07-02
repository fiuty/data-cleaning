package com.chebianjie.datacleaning.repository;


import com.chebianjie.datacleaning.domain.UtUserCars;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data  repository for the UtUserCars entity.
 */
@Repository
@Transactional
public interface UtUserCarsRepository extends JpaSpecificationExecutor<UtUserCars>, JpaRepository<UtUserCars, Long> {


//    Page<UtUserCars> findAll(Pageable pageable);

        List<UtUserCars> findAllByUid(Long Uid);





}
