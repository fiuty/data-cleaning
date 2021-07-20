package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.enums.Platform;

import java.util.List;

public interface ConsumerLogService {

    ConsumerLog getOneByUnionIdAndStatusAndType(String unionid, int status, int type);

    ConsumerLog getOneByCbjAccountAndStatusAntType(String cbjAccount, int status, int type);

    ConsumerLog getOneByCbjIdAndStatusAndType(long cbjId, int status, int type);

    ConsumerLog getOneByChjAccountAndStatusAndType(String chjAccount, int status, int type);

    ConsumerLog getOneByChjIdAndStatusAndType(long chjId, int status, int type);

    ConsumerLog getOneByConsumerIdAndStatusAndType(long consumerId, int status, int type);

    ConsumerLog findOneByUnionidAndStatusAndType(String unionid, int status, int type);

    ConsumerLog findOneByCbjAccountAndStatusAndType(String phone, int status, int type);

    ConsumerLog findOneByChjAccountAndStatusAndType(String phone, int status, int type);

    ConsumerLog findOneByConsumerId(Long id,Integer type);

    void saveOne(ConsumerLog consumerLog);

    int countByUnionId(String unionid, int type, int status);

    int countByCbjAccount(String cbjAccount, int type, int status);

    ConsumerLog findOneByCbjIdOrChjId(Long id, Platform platform);

    List<ConsumerLog> findAllByConsumerIdInAndType(List<Long> consumerIds, int type);
}
