package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerLog;

public interface ConsumerLogService {

    ConsumerLog getOneByUnionId(String unionid, int type, int status);

    ConsumerLog getOneByCbjAccount(String cbjAccount, int type, int status);

    void saveOne(ConsumerLog consumerLog);

    int countByUnionId(String unionid, int type, int status);

    int countByCbjAccount(String cbjAccount, int type, int status);
}
