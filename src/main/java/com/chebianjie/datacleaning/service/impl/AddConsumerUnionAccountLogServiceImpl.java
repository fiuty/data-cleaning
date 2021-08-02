package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.AddConsumerUnionAccountLog;
import com.chebianjie.datacleaning.repository.AddConsumerUnionAccountLogRepository;
import com.chebianjie.datacleaning.service.AddConsumerUnionAccountLogService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AddConsumerUnionAccountLogServiceImpl implements AddConsumerUnionAccountLogService {

    @Autowired
    AddConsumerUnionAccountLogRepository addConsumerUnionAccountLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public AddConsumerUnionAccountLog saveOne(int type,Long cbjId,Long chjId,String consumerUnionAccount,int status) {
        AddConsumerUnionAccountLog addlog = new AddConsumerUnionAccountLog();
        addlog.setType(type);
        addlog.setCbjId(cbjId);
        addlog.setChjId(chjId);
        addlog.setConsumerUnionAccount(consumerUnionAccount);
        addlog.setCreateDate(LocalDateTime.now());
        addlog.setStatus(status);
        return addConsumerUnionAccountLogRepository.save(addlog);
    }


}
