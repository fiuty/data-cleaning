package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtUserDriverLicens;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UtUserDriverLicensService {


    public Page<UtUserDriverLicens> getCbjAllUtUserDriverLicens(Pageable pageable);

    public void updateCbjUtUserDriverLicensById(String consumerUnionAccount,Long id);

    public Page<UtUserDriverLicens> getChjAllUtUserDriverLicens(Pageable pageable);

    public void updateChjUtUserDriverLicensById(String consumerUnionAccount,Long id);



}
