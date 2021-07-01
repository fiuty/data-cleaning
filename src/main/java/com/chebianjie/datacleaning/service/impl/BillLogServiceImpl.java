package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.BillLog;
import com.chebianjie.datacleaning.repository.BillLogRepository;
import com.chebianjie.datacleaning.service.BillLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhengdayue
 * @date: 2021-07-01
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BillLogServiceImpl implements BillLogService {

    @Autowired
    private BillLogRepository billLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void save(String unionAccount, Integer status) {
        BillLog billLog = new BillLog();
        billLog.setUnionAccount(unionAccount);
        billLog.setStatus(status);
        billLogRepository.save(billLog);
    }
}
