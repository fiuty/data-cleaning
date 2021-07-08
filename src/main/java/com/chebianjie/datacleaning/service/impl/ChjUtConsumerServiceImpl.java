package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.service.ChjUtConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ChjUtConsumerServiceImpl implements ChjUtConsumerService {

    @Autowired
    private UtConsumerRepository utConsumerRepository;

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer getUtConsumerById(Long id) {
        return utConsumerRepository.findById(id).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtConsumer> pageUtConsumer(Pageable pageable) {
        return utConsumerRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer getUtConsumerByUnionid(String unionid) {
        return utConsumerRepository.findByUnionid(unionid);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer getUtConsumerByAccount(String account) {
        return utConsumerRepository.findByAccount(account);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtConsumer> getUtConsumerListByAccount(String account) {
        return utConsumerRepository.findListByAccount(account);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtConsumer> getUtConsumerListByUnionid(String unionid) {
        return utConsumerRepository.findListByUnionid(unionid);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer getUtConsumerByPhone(String phone) {
        return utConsumerRepository.findByAccount(phone);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtConsumer> listByUnionid(String unionid) {
        return utConsumerRepository.findListByUnionid(unionid);
    }
}
