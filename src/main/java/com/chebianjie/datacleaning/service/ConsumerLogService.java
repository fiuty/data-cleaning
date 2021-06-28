package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerLog;

public interface ConsumerLogService {

    ConsumerLog getOneByUnionId(String unionid);

    ConsumerLog getOneByCbjAccount(String cbjAccount);

    void saveOne(ConsumerLog consumerLog);
}
