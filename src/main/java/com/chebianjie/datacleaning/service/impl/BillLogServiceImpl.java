package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.common.core.util.CollectUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.BillLog;
import com.chebianjie.datacleaning.repository.BillLogRepository;
import com.chebianjie.datacleaning.service.BillLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<BillLog> findByStatus(Integer status) {
         return billLogRepository.findAllByStatus(status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Boolean repeatClean(String unionAccount) {
        return billLogRepository.findByUnionAccountAndStatus(unionAccount, 1) != null;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void deleteAll(List<BillLog> billLogs) {
        billLogRepository.deleteAll(billLogs);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveAll(List<String> unionAccounts, Integer status) {
        List<BillLog> billLogs = unionAccounts.stream().map(unionAccount -> {
            BillLog billLog = new BillLog();
            billLog.setUnionAccount(unionAccount);
            billLog.setStatus(status);
            return billLog;
        }).collect(Collectors.toList());
        billLogRepository.batchInsert(billLogs);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Map<String, Boolean> batchRepeatClean(List<String> unionAccounts) {
        List<BillLog> billLogs = billLogRepository.findAllByUnionAccountIn(unionAccounts);
        if (CollectUtil.collectionNotEmpty(billLogs)) {
            List<String> billLogUnionAccount = billLogs.stream().map(BillLog::getUnionAccount).collect(Collectors.toList());
            HashMap<String, Boolean> map = new HashMap<>(unionAccounts.size());
            for (String unionAccount : unionAccounts) {
                map.put(unionAccount, billLogUnionAccount.contains(unionAccount));
            }
            return map;
        }
        HashMap<String, Boolean> map = new HashMap<>(unionAccounts.size());
        for (String unionAccount : unionAccounts) {
            map.put(unionAccount, false);
        }
        return map;
    }
}
