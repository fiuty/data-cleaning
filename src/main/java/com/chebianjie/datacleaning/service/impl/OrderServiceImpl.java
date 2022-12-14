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

    private static int cbjSelfTotal = 0;

    private static int cbjDustTotal = 0;

    private static int cbjAutoTotal = 0;

    private static int cbjChargeTotal = 0;

    private static int chjSelfTotal = 0;

    private static int chjDustTotal = 0;

    private static int chjAutoTotal = 0;

    private static int chjChargeTotal = 0;


    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleaningCBJWashOrder(Long consumerId, String phone, String consumerAccount) {
        log.info("==========清洗车便捷用户id：{}，手机号：{}，唯一标识：{}========", consumerId, phone, consumerAccount);
        // 自助洗车
        List<UtConsump> utConsumps = utConsumpRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utConsumps)) {
            log.info("清洗用户自助洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utConsumps.size());
            Integer updateResult = utConsumpRepository.updateConsumerAccount(consumerId, consumerAccount);
            cbjSelfTotal += updateResult;
        }
        // 自助吸尘
        List<DushOrder> dushOrders = dushOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(dushOrders)) {
            log.info("清洗用户自助吸尘，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, dushOrders.size());
            Integer updateResult = dushOrderRepository.updateConsumerAccount(consumerId, consumerAccount);
            cbjDustTotal += updateResult;
        }
        // 全自动洗车
        List<AutoOrder> autoOrders = autoOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(autoOrders)) {
            log.info("清洗用户全自动洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, autoOrders.size());
            Integer updateResult = autoOrderRepository.updateConsumerAccount(consumerId, consumerAccount);
            cbjAutoTotal += updateResult;
        }
        // 充值订单
        List<UtChargeLog> utChargeLogList = utChargeLogRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utChargeLogList)) {
            log.info("清洗用户充值订单，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utChargeLogList.size());
            Integer updateResult = utChargeLogRepository.updateConsumerAccount(consumerId, consumerAccount);
            cbjChargeTotal += updateResult;
        }
        log.info("-------》车便捷清洗完成数量：自助：{}，吸尘：{}。全自动：{}，充值：{}", cbjSelfTotal, cbjDustTotal, cbjAutoTotal, cbjChargeTotal);
    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    public void cleaningCHJWashOrder(Long consumerId, String phone, String consumerAccount) {
        log.info("==========清洗车惠捷用户id：{}，手机号：{}，唯一标识：{}========", consumerId, phone, consumerAccount);
        // 自助洗车
        List<UtConsump> utConsumps = utConsumpRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utConsumps)) {
            log.info("清洗用户自助洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utConsumps.size());
            Integer updateResult = utConsumpRepository.updateConsumerAccount(consumerId, consumerAccount);
            chjSelfTotal += updateResult;
        }
        // 自助吸尘
        List<DushOrder> dushOrders = dushOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(dushOrders)) {
            log.info("清洗用户自助吸尘，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, dushOrders.size());
            Integer updateResult = dushOrderRepository.updateConsumerAccount(consumerId, consumerAccount);
            chjDustTotal += updateResult;
        }
        // 全自动洗车
        List<AutoOrder> autoOrders = autoOrderRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(autoOrders)) {
            log.info("清洗用户全自动洗车，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, autoOrders.size());
            Integer updateResult = autoOrderRepository.updateConsumerAccount(consumerId, consumerAccount);
            chjAutoTotal += updateResult;
        }
        // 充值订单
        List<UtChargeLog> utChargeLogList = utChargeLogRepository.findAllByConsumerId(consumerId);
        if (CollectionUtil.isNotEmpty(utChargeLogList)) {
            log.info("清洗用户充值订单，用户id：{}，手机号：{}，唯一标识：{},数量：{}", consumerId, phone, consumerAccount, utChargeLogList.size());
            Integer updateResult = utChargeLogRepository.updateConsumerAccount(consumerId, consumerAccount);
            chjChargeTotal += updateResult;
        }
        log.info("-------》车惠捷清洗完成数量：自助：{}，吸尘：{}，全自动：{}，充值：{}", chjSelfTotal, chjDustTotal, chjAutoTotal, chjChargeTotal);
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
            resultList = dushOrderRepository.findByStartTimePage(startTime, consumerId);
        }
        if (type == 3) {
            resultList = autoOrderRepository.findByStartTimePage(startTime, consumerId);
        }
        if (type == 4) {
            resultList = utChargeLogRepository.findByStartTimePage(startTime, consumerId);
        }
        return resultList;
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cbjUpdateOrder(Integer type, Map<String, Object> map, Long startTime) {
        Long consumerId = (Long) map.get("consumerId");
        String consumerAccount = (String) map.get("consumerAccount");
        String phone = (String) map.get("phone");
        if (type == 1) {
            log.info("更新自助洗车订单：consumerId：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            utConsumpRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }
        if (type == 2) {
            log.info("更新自助吸尘订单：id：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            dushOrderRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }
        if (type == 3) {
            log.info("更新全自动洗车订单：id：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            autoOrderRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }
        if (type == 4) {
            log.info("更新用户充值订单：id：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            utChargeLogRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }

    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ConsumerPhoneDTO> chjOrderTotal(Integer type, Long startTime) {
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
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List findCHJOrderByPage(Integer type, Long startTime, Long consumerId) {
        // type 1.自助洗车  2.自助吸尘  3.全自动洗车
        List resultList = null;
        if (type == 1) {
            resultList = utConsumpRepository.findByStartTimePage(startTime, consumerId);
        }
        if (type == 2) {
            resultList = dushOrderRepository.findByStartTimePage(startTime, consumerId);
        }
        if (type == 3) {
            resultList = autoOrderRepository.findByStartTimePage(startTime, consumerId);
        }
        if (type == 4) {
            resultList = utChargeLogRepository.findByStartTimePage(startTime, consumerId);
        }
        return resultList;
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_ORDER)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void chjUpdateOrder(Integer type, Map<String, Object> map, Long startTime) {
        Long consumerId = (Long) map.get("consumerId");
        String consumerAccount = (String) map.get("consumerAccount");
        String phone = (String) map.get("phone");
        if (type == 1) {
            log.info("更新自助洗车订单：consumerId：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            utConsumpRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }
        if (type == 2) {
            log.info("更新自助吸尘订单：id：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            dushOrderRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }
        if (type == 3) {
            log.info("更新全自动洗车订单：id：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            autoOrderRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }
        if (type == 4) {
            log.info("更新用户充值订单：id：{}，phone：{}，consumerAccount：{}", consumerId, phone, consumerAccount);
            utChargeLogRepository.updateConsumerAccountByStartTime(consumerId, consumerAccount, startTime);
        }

    }




}

