package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumerCouponLog;
import com.chebianjie.datacleaning.repository.ConsumerCouponLogRepository;
import com.chebianjie.datacleaning.service.ConsumerCouponLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerCouponLogServiceImpl implements ConsumerCouponLogService {

    @Autowired
    private ConsumerCouponLogRepository consumerCouponLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveOne(ConsumerCouponLog consumerCouponLog) {
        consumerCouponLogRepository.save(consumerCouponLog);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerCouponLog getOneByUtCouponUserIdAndStatusAndType(long utCouponUserId, int status, int type) {
        return consumerCouponLogRepository.findOneByUtCouponUserIdAndStatusAndType(utCouponUserId, status, type);
    }

}
