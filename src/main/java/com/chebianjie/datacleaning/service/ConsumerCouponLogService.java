package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.ConsumerCouponLog;

public interface ConsumerCouponLogService {

    void saveOne(ConsumerCouponLog consumerCouponLog);

    ConsumerCouponLog getOneByUtCouponUserIdAndStatusAndType(long utCouponUserId, int status, int type);
}
