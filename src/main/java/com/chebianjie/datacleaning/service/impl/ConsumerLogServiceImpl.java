package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerLogServiceImpl implements ConsumerLogService {

    @Autowired
    private ConsumerLogRepository consumerLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByUnionIdAndStatusAndType(String unionid, int status, int type) {
        return consumerLogRepository.findOneByUnionidAndStatusAndType(unionid, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByCbjAccountAndStatusAntType(String cbjAccount, int status, int type) {
        return consumerLogRepository.findOneByCbjAccountAndStatusAndType(cbjAccount, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByCbjIdAndStatusAndType(long cbjId, int status, int type) {
        return consumerLogRepository.findOneByCbjIdAndStatusAndType(cbjId, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByChjAccountAndStatusAndType(String chjAccount, int status, int type) {
        return consumerLogRepository.findOneByChjAccountAndStatusAndType(chjAccount, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByChjIdAndStatusAndType(long chjId, int status, int type) {
        return consumerLogRepository.findOneByChjIdAndStatusAndType(chjId, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog getOneByConsumerIdAndStatusAndType(long consumerId, int status, int type) {
        return consumerLogRepository.findOneByConsumerIdAndStatusAndType(consumerId, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void saveOne(ConsumerLog consumerLog) {
        consumerLogRepository.save(consumerLog);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public int countByUnionId(String unionid, int type, int status) {
        return consumerLogRepository.countByUnionidAndTypeAndStatus(unionid, type, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public int countByCbjAccount(String cbjAccount, int type, int status) {
        return consumerLogRepository.countByCbjAccountAndTypeAndStatus(cbjAccount, type, status);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog findOneByUnionidAndStatusAndType(String unionid, int status, int type) {
        return consumerLogRepository.findOneByUnionidAndStatusAndType(unionid, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog findOneByCbjAccountAndStatusAndType(String phone, int status, int type) {
        return consumerLogRepository.findOneByCbjAccountAndStatusAndType(phone, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog findOneByChjAccountAndStatusAndType(String phone, int status, int type) {
        return consumerLogRepository.findOneByChjAccountAndStatusAndType(phone, status, type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog findOneByConsumerId(Long id,Integer type) {
        return consumerLogRepository.findOneByConsumerIdAndType(id,type);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerLog findOneByCbjIdOrChjId(Long id, Platform platform) {
        if (platform == Platform.CHEHUIJIE) {
            return consumerLogRepository.findOneByChjIdAndStatusAndType(id, 1, 1);
        } else {
            return consumerLogRepository.findOneByCbjIdAndStatusAndType(id, 1, 1);
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<ConsumerLog> findAllByConsumerIdInAndType(List<Long> consumerIds, int type) {
        return consumerLogRepository.findAllByConsumerIdInAndType(consumerIds, type);
    }


    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<ConsumerLog> getCbjConsumerLogByConsumerId(Long cbjConsumerId) {
        return consumerLogRepository.findAllByCbjIdAndStatusAndType(cbjConsumerId,1,1);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<ConsumerLog> getChjConsumerLogByConsumerId(Long chjConsumerId) {
        return consumerLogRepository.findAllByChjIdAndStatusAndType(chjConsumerId,1,1);
    }










}
