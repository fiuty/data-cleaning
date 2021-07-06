package com.chebianjie.datacleaning.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.UtChargeLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.order.AutoOrder;
import com.chebianjie.datacleaning.domain.order.DushOrder;
import com.chebianjie.datacleaning.domain.order.UtConsump;
import com.chebianjie.datacleaning.repository.ConsumerRepository;
import com.chebianjie.datacleaning.repository.UtChargeLogRepository;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.repository.order.AutoOrderRepository;
import com.chebianjie.datacleaning.repository.order.DushOrderRepository;
import com.chebianjie.datacleaning.repository.order.UtConsumpRepository;
import com.chebianjie.datacleaning.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2021-06-28
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UtConsumpRepository utConsumpRepository;
    @Autowired
    private DushOrderRepository dushOrderRepository;
    @Autowired
    private AutoOrderRepository autoOrderRepository;
    @Autowired
    private UtChargeLogRepository utChargeLogRepository;


    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleaningCBJWashOrder(Long consumerId, String phone, String consumerAccount) {
        log.info("==========清洗车便捷用户id：{}，手机号：{}，唯一标识：{}========", consumerId, phone, consumerAccount);
        // 自助洗车
        List<UtConsump> utConsumps = utConsumpRepository.findAllByConsumerId(consumerId.intValue());
        if (CollectionUtil.isNotEmpty(utConsumps)) {
            log.info("清洗用户自助洗车，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (UtConsump utConsump : utConsumps) {
                utConsump.setConsumerAccount(consumerAccount);
                utConsumpRepository.saveAndFlush(utConsump);
            }
        }

        // 自助吸尘
        List<DushOrder> dushOrders = dushOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(dushOrders)) {
            log.info("清洗用户自助吸尘，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (DushOrder dushOrder : dushOrders) {
                dushOrder.setConsumerAccount(consumerAccount);
                dushOrderRepository.saveAndFlush(dushOrder);
            }
        }

        // 全自动洗车
        List<AutoOrder> autoOrders = autoOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(autoOrders)) {
            log.info("清洗用户全自动洗车，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (AutoOrder autoOrder : autoOrders) {
                autoOrder.setConsumerAccount(consumerAccount);
                autoOrderRepository.saveAndFlush(autoOrder);
            }
        }
        // 充值订单
        List<UtChargeLog> utChargeLogList = utChargeLogRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utChargeLogList)) {
            log.info("清洗用户充值订单，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (UtChargeLog utChargeLog : utChargeLogList) {
                utChargeLog.setConsumerAccount(consumerAccount);
                utChargeLogRepository.saveAndFlush(utChargeLog);
            }
        }
    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    public void cleaningCHJWashOrder(Long consumerId, String phone, String consumerAccount) {
        log.info("==========清洗车惠捷用户id：{}，手机号：{}，唯一标识：{}========", consumerId, phone, consumerAccount);
        // 自助洗车
        List<UtConsump> utConsumps = utConsumpRepository.findAllByConsumerId(consumerId.intValue());
        if (CollectionUtil.isNotEmpty(utConsumps)) {
            log.info("清洗用户自助洗车，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (UtConsump utConsump : utConsumps) {
                utConsump.setConsumerAccount(consumerAccount);
                utConsumpRepository.saveAndFlush(utConsump);
            }
        }

        // 自助吸尘
        List<DushOrder> dushOrders = dushOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(dushOrders)) {
            log.info("清洗用户自助吸尘，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (DushOrder dushOrder : dushOrders) {
                dushOrder.setConsumerAccount(consumerAccount);
                dushOrderRepository.saveAndFlush(dushOrder);
            }
        }

        // 全自动洗车
        List<AutoOrder> autoOrders = autoOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(autoOrders)) {
            log.info("清洗用户全自动洗车，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (AutoOrder autoOrder : autoOrders) {
                autoOrder.setConsumerAccount(consumerAccount);
                autoOrderRepository.saveAndFlush(autoOrder);
            }
        }
        // 充值订单
        List<UtChargeLog> utChargeLogList = utChargeLogRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utChargeLogList)) {
            log.info("清洗用户充值订单，用户id：{}，手机号：{}，唯一标识：{}", consumerId, phone, consumerAccount);
            for (UtChargeLog utChargeLog : utChargeLogList) {
                utChargeLog.setConsumerAccount(consumerAccount);
                utChargeLogRepository.saveAndFlush(utChargeLog);
            }
        }
    }



}

