package com.chebianjie.datacleaning.service;


import com.chebianjie.datacleaning.domain.UtConsumerBalanceClearLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UtConsumerBalanceClearLogService {


    public Page<UtConsumerBalanceClearLog> getCbjAllUtConsumerBalanceClearLog(Pageable pageable);

    public void updateCbjUtConsumerBalanceClearLogById(String consumerUnionAccount,Long id);

    public Page<UtConsumerBalanceClearLog> getChjAllUtConsumerBalanceClearLog(Pageable pageable);

    public void updateChjUtConsumerBalanceClearLogById(String consumerUnionAccount,Long id);



}
