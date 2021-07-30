package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.service.CbjUtConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CbjUtConsumerServiceImpl implements CbjUtConsumerService {

    @Autowired
    private UtConsumerRepository utConsumerRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer getUtConsumerById(Long id) {
        return utConsumerRepository.findById(id).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer getUtConsumerByPhone(String phone) {
        return utConsumerRepository.findByAccount(phone);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public List<UtConsumer> getUtConsumerListByUnionid(String unionid) {
        return utConsumerRepository.findListByUnionid(unionid);
    }

    @Override
    public List<UtConsumer> getUtConsumerListByAccount(String account) {
        return utConsumerRepository.findListByAccount(account);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtConsumer> pageUtConsumer(Pageable pageable) {
        return utConsumerRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public int countByCreatetimeLessThanEqual(Long consumerTime) {
        return utConsumerRepository.countByCreatetimeLessThanEqual(consumerTime);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public List<UtConsumer> findAllByPage(int pageNumber, int pageSize) {
        return utConsumerRepository.findAllByPage(pageNumber, pageSize);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public int countByUnionidAndStatue(String unionid, int statue) {
        return utConsumerRepository.countByUnionidAndStatue(unionid, statue);
    }
}
