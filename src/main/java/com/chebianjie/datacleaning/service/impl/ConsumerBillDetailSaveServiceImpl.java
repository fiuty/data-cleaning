package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumerBillChangeDetail;
import com.chebianjie.datacleaning.repository.ConsumerBillChangeDetailRepository;
import com.chebianjie.datacleaning.service.ConsumerBillDetailSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
@Service
public class ConsumerBillDetailSaveServiceImpl implements ConsumerBillDetailSaveService {

    @Autowired
    private ConsumerBillChangeDetailRepository consumerBillChangeDetailRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ConsumerBillChangeDetail consumerBillChangeDetail) {
        consumerBillChangeDetailRepository.save(consumerBillChangeDetail);
    }
}
