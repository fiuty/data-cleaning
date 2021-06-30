package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtChargeLog;
import com.chebianjie.datacleaning.repository.UtChargeLogRepository;
import com.chebianjie.datacleaning.service.ChargeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ChargeLogServiceImpl implements ChargeLogService {

    @Autowired
    private UtChargeLogRepository utChargeLogRepository;

    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    public UtChargeLog cbjFindById(Long id) {
        return utChargeLogRepository.findById(id).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    public UtChargeLog chjFindById(Long id) {
        return utChargeLogRepository.findById(id).orElse(null);
    }
}
