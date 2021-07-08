package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerLog;

public interface ConsumerLogService {

    ConsumerLog getOneByUnionId(String unionid, int type, int status);

    ConsumerLog getOneByCbjAccount(String cbjAccount, int type, int status);

    ConsumerLog getOneByCbjIdAndStatusAndType(long cbjId, int status, int type);

    ConsumerLog getOneByChjIdAndStatusAndType(long chjId, int status, int type);

    ConsumerLog getOneByConsumerIdAndStatusAndType(long consumerId, int status, int type);

    void saveOne(ConsumerLog consumerLog);

    int countByUnionId(String unionid, int type, int status);

    int countByCbjAccount(String cbjAccount, int type, int status);

    ConsumerLog findOneByUnionidAndStatusAndType(String unionid, int status, int type);

    ConsumerLog findOneByCbjAccountAndStatusAndType(String phone, int status, int type);

    ConsumerLog findOneByChjAccountAndStatusAndType(String phone, int status, int type);
}
