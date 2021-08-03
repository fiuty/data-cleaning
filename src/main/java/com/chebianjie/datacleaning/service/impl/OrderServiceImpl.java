package com.chebianjie.datacleaning.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtChargeLog;
import com.chebianjie.datacleaning.domain.order.AutoOrder;
import com.chebianjie.datacleaning.domain.order.DushOrder;
import com.chebianjie.datacleaning.domain.order.UtConsump;
import com.chebianjie.datacleaning.dto.ConsumerPhoneDTO;
import com.chebianjie.datacleaning.repository.UtChargeLogRepository;
import com.chebianjie.datacleaning.repository.order.AutoOrderRepository;
import com.chebianjie.datacleaning.repository.order.DushOrderRepository;
import com.chebianjie.datacleaning.repository.order.UtConsumpRepository;
import com.chebianjie.datacleaning.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<UtConsump> utConsumps = utConsumpRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utConsumps)) {
            log.info("清洗用户自助洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utConsumps.size());
            for (UtConsump utConsump : utConsumps) {
                Integer updateResult = utConsumpRepository.updateConsumerAccount(utConsump.getId(), consumerAccount);
//                log.info("保存自助洗车订单结果：{}", updateResult);
            }
        }

        // 自助吸尘
        List<DushOrder> dushOrders = dushOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(dushOrders)) {
            log.info("清洗用户自助吸尘，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, dushOrders.size());
            for (DushOrder dushOrder : dushOrders) {
                Integer updateResult = dushOrderRepository.updateConsumerAccount(dushOrder.getId(), consumerAccount);
//                log.info("保存自助吸尘订单结果：{}", updateResult);
            }
        }

        // 全自动洗车
        List<AutoOrder> autoOrders = autoOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(autoOrders)) {
            log.info("清洗用户全自动洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, autoOrders.size());
            for (AutoOrder autoOrder : autoOrders) {
                Integer updateResult = autoOrderRepository.updateConsumerAccount(autoOrder.getId(), consumerAccount);
//                log.info("保存自动洗车订单结果：{}", updateResult);
            }
        }
        // 充值订单
        List<UtChargeLog> utChargeLogList = utChargeLogRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utChargeLogList)) {
            log.info("清洗用户充值订单，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utChargeLogList.size());
            for (UtChargeLog utChargeLog : utChargeLogList) {
                Integer updateResult = utChargeLogRepository.updateConsumerAccount(utChargeLog.getId(), consumerAccount);
//                log.info("保存用户充值订单结果：{}", updateResult);
            }
        }
    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    public void cleaningCHJWashOrder(Long consumerId, String phone, String consumerAccount) {
        log.info("==========清洗车惠捷用户id：{}，手机号：{}，唯一标识：{}========", consumerId, phone, consumerAccount);
        // 自助洗车
        List<UtConsump> utConsumps = utConsumpRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utConsumps)) {
            log.info("清洗用户自助洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utConsumps.size());
            for (UtConsump utConsump : utConsumps) {
                Integer updateResult = utConsumpRepository.updateConsumerAccount(utConsump.getId(), consumerAccount);
//                log.info("保存自助洗车订单结果：{}", updateResult);
            }
        }

        // 自助吸尘
        List<DushOrder> dushOrders = dushOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(dushOrders)) {
            log.info("清洗用户自助吸尘，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, dushOrders.size());
            for (DushOrder dushOrder : dushOrders) {
                Integer updateResult = dushOrderRepository.updateConsumerAccount(dushOrder.getId(), consumerAccount);
            }
        }

        // 全自动洗车
        List<AutoOrder> autoOrders = autoOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(autoOrders)) {
            log.info("清洗用户全自动洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, autoOrders.size());
            for (AutoOrder autoOrder : autoOrders) {
                Integer updateResult = autoOrderRepository.updateConsumerAccount(autoOrder.getId(), consumerAccount);
//                log.info("保存自动洗车订单结果：{}", updateResult);
            }
        }
        // 充值订单
        List<UtChargeLog> utChargeLogList = utChargeLogRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utChargeLogList)) {
            log.info("清洗用户充值订单，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utChargeLogList.size());
            for (UtChargeLog utChargeLog : utChargeLogList) {
                Integer updateResult = utChargeLogRepository.updateConsumerAccount(utChargeLog.getId(), consumerAccount);
//                log.info("保存用户充值订单结果：{}", updateResult);
            }
        }
    }


    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ConsumerPhoneDTO> cbjOrderTotal(Integer type, Long startTime) {
        List<ConsumerPhoneDTO> resultList = null;
        // type 1.自助洗车  2.自助吸尘  3.全自动洗车
        if (type == 1) {
            resultList = utConsumpRepository.findCountByStartTime(startTime);
        }
        if (type == 2) {
            resultList = dushOrderRepository.findCountByStartTime(startTime);
        }
        if (type == 3) {
            resultList = autoOrderRepository.findCountByStartTime(startTime);
        }
        if (type == 4) {
            resultList = utChargeLogRepository.findCountByStartTime(startTime);
        }
        return resultList;
    }


    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List findCBJOrderByPage(Integer type, Long startTime, Long consumerId) {
        // type 1.自助洗车  2.自助吸尘  3.全自动洗车
        List resultList = null;
        if (type == 1) {
            resultList = utConsumpRepository.findByStartTimePage(startTime, consumerId);
        }
        if (type == 2) {
            resultList = dushOrderRepository.findByStartTimePage(startTime);
        }
        if (type == 3) {
            resultList = autoOrderRepository.findByStartTimePage(startTime);
        }
        if (type == 4) {
            resultList = utChargeLogRepository.findByStartTimePage(startTime);
        }
        return resultList;
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cbjUpdateOrder(Integer type, List<Map<String, Object>> dataList, Long startTime) {
        for (Map<String, Object> map : dataList) {
            Long id = (Long) map.get("id");
            String consumerAccount = (String) map.get("consumerAccount");
            String phone = (String) map.get("phone");
            if (type == 1) {
                log.info("更新自助洗车订单：id：{}，phone：{}，consumerAccount：{}", id, phone, consumerAccount);
                utConsumpRepository.updateConsumerAccountByStartTime(id, consumerAccount, startTime);
            }
            if (type == 2) {
                log.info("更新自助吸尘订单：id：{}，phone：{}，consumerAccount：{}", id, phone, consumerAccount);
                dushOrderRepository.updateConsumerAccountByStartTime(id, consumerAccount, startTime);
            }
            if (type == 3) {
                log.info("更新全自动洗车订单：id：{}，phone：{}，consumerAccount：{}", id, phone, consumerAccount);
                autoOrderRepository.updateConsumerAccountByStartTime(id, consumerAccount, startTime);
            }
            if (type == 4) {
                log.info("更新用户充值订单：id：{}，phone：{}，consumerAccount：{}", id, phone, consumerAccount);
                utChargeLogRepository.updateConsumerAccountByStartTime(id, consumerAccount, startTime);
            }
        }


    }
}

