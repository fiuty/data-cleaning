package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumerBill;
import com.chebianjie.datacleaning.repository.ConsumerBillRepository;
import com.chebianjie.datacleaning.service.ConsumerBillSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhengdayue
 * @date: 2021-07-07
 */
@Service
public class ConsumerBillSaveServiceImpl implements ConsumerBillSaveService {

    @Autowired
    private ConsumerBillRepository consumerBillRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ConsumerBill consumerBill) {
        consumerBillRepository.save(consumerBill);
    }
}
