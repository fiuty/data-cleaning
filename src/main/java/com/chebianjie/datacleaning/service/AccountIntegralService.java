package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.AccountIntegral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountIntegralService {


    public Page<AccountIntegral> getCbjAllAccountIntegral(Pageable pageable);

    public void updateCbjAccountIntegralById(String consumerUnionAccount,Long id);

    public Page<AccountIntegral> getChjAllAccountIntegral(Pageable pageable);

    public void updateChjAccountIntegralById(String consumerUnionAccount,Long id);




}
