package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumerLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface UtConsumerLogService {


    public Page<UtConsumerLog> getCbjAllUtConsumerLog(Pageable pageable);

    public void updateCbjUtConsumerLog(String consumerUnionAccount,Long id);

    public Page<UtConsumerLog> getChjAllUtConsumerLog(Pageable pageable);

    public void updateChjUtConsumerLog(String consumerUnionAccount,Long id);

}
