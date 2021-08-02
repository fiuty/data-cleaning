package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumeCardUseDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsumeCardUseDetailService {


    public Page<ConsumeCardUseDetail> getCbjAllConsumeCardUseDetail(Pageable pageable);

    public void updateCbjConsumeCardUseDetailById(String consumerUnionAccount,Long id);

    public Page<ConsumeCardUseDetail> getChjAllConsumeCardUseDetail(Pageable pageable);

    public void updateChjConsumeCardUseDetailById(String consumerUnionAccount,Long id);






}
