package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.DataSynTime;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.DataSynTimeRepository;
import com.chebianjie.datacleaning.repository.UtUserTotalFlowRepository;
import com.chebianjie.datacleaning.service.DataSynTimeService;
import com.chebianjie.datacleaning.service.UtUserTotalFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class UtUserTotalFlowServiceImpl implements UtUserTotalFlowService {

    @Autowired
    private UtUserTotalFlowRepository utUserTotalFlowRepository;

    @Autowired
    private DataSynTimeRepository dataSynTimeRepository;

    @Autowired
    private DataSynTimeService dataSynTimeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public List<UtUserTotalFlow> cbjFindAllByUid(Long cbjId) {
        return utUserTotalFlowRepository.findAllByUid(cbjId);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtUserTotalFlow> chjFindAllByUid(Long chjId) {
        return utUserTotalFlowRepository.findAllByUid(chjId);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public List<UtUserTotalFlow> cbjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize) {
        return utUserTotalFlowRepository.findAllByCreateTimeBetweenPage(timeFrom, timeTo, pageNumber * pageSize, pageSize);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtUserTotalFlow> chjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize) {
        return utUserTotalFlowRepository.findAllByCreateTimeBetweenPage(timeFrom, timeTo, pageNumber * pageSize, pageSize);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public int cbjCountByCreateTimeBetween(Long timeFrom, Long timeTo) {
        return utUserTotalFlowRepository.countByCreateTimeBetween(timeFrom, timeTo);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public int chjCountByCreateTimeBetween(Long timeFrom, Long timeTo) {
        return utUserTotalFlowRepository.countByCreateTimeBetween(timeFrom, timeTo);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void utUserTotalFlowJob() {
        DataSynTime dataSynTime = dataSynTimeService.findBySynType(6);
        Long timeFrom = dataSynTime.getLastTime();
        LocalDateTime timeTo = LocalDateTime.now().minus(Duration.ofSeconds(5));
        int pageSize = 1000;
        int cbjTotal = this.cbjCountByCreateTimeBetween(timeFrom, toEpochMilli(timeTo));
        int cbjTotalPage = computeTotalPage(cbjTotal);
        for (int pageNumber = 0; pageNumber <= cbjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtUserTotalFlow> cbjUtUserTotalFlows = this.cbjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            convertAndSend(cbjUtUserTotalFlows, Platform.CHEBIANJIE);
            Instant end = Instant.now();
            log.info("用户余额清洗-车便捷流水增量总页数:{},第：{}页,总用时：{} s", cbjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        DataSynTime chjDataSynTime = dataSynTimeService.findBySynType(7);
        Long chjTimeFrom = chjDataSynTime.getLastTime();
        int chjTotal = this.chjCountByCreateTimeBetween(chjTimeFrom, toEpochMilli(timeTo));
        int chjTotalPage = computeTotalPage(chjTotal);
        for (int pageNumber = 0; pageNumber <= chjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtUserTotalFlow> chjUtUserTotalFlows = this.chjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            convertAndSend(chjUtUserTotalFlows, Platform.CHEHUIJIE);
            Instant end = Instant.now();
            log.info("用户余额清洗-车惠捷流水增量总页数:{},第：{}页,总用时：{} s", chjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        dataSynTime.setLastTime(toEpochMilli(timeTo));
        chjDataSynTime.setLastTime(toEpochMilli(timeTo));
        dataSynTimeService.updateDataSynTime(chjDataSynTime);
        dataSynTimeService.updateDataSynTime(dataSynTime);
        log.info("用户余额清洗-同步增量,车便捷流水增量cbjTotal：{}，车惠捷流水增量chjTotal：{}，起始时间timeFrom：{}，截止时间timeTo：{}", cbjTotal, chjTotal, timeFrom, timeTo);
    }

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtUserTotalFlow cbjFindById(Long id) {
        return utUserTotalFlowRepository.findById(id).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtUserTotalFlow chjFindById(Long id) {
        return utUserTotalFlowRepository.findById(id).orElse(null);
    }

    private void convertAndSend(List<UtUserTotalFlow> cbjUtUserTotalFlows, Platform platform) {
        cbjUtUserTotalFlows.forEach(flow -> {
            flow.setPlatform(platform);
            rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_FLOW_EXCHANGE, RabbitMqConstants.DATA_CLEAN_FLOW_ROUTING_KEY, flow);
        });
    }

    private int computeTotalPage(long total) {
        int pageSize = 1000;
        int totalPage = (int)total / pageSize;
        long mod = total % pageSize;
        if (mod != 0) {
            ++totalPage;
        }
        return totalPage;
    }

    private Long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
