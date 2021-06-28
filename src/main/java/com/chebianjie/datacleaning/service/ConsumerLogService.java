package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerLog;

public interface ConsumerLogService {

    ConsumerLog getOneByUnionId(String unionid, int status);

    ConsumerLog getOneByCbjAccount(String cbjAccount, int status);

    void saveOne(ConsumerLog consumerLog);
}
