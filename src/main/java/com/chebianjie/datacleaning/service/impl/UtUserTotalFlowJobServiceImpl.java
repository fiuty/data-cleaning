package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.DataSynTime;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.service.DataSynTimeService;
import com.chebianjie.datacleaning.service.UtUserTotalFlowJobService;
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

/**
 * @author zhengdayue
 * @date: 2021-08-03
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtUserTotalFlowJobServiceImpl implements UtUserTotalFlowJobService {


    @Autowired
    private DataSynTimeService dataSynTimeService;

    @Autowired
    private UtUserTotalFlowService utUserTotalFlowService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void utUserTotalFlowJob() {
        DataSynTime dataSynTime = dataSynTimeService.findBySynType(6);
        Long timeFrom = dataSynTime.getLastTime();
        LocalDateTime timeTo = LocalDateTime.now().minus(Duration.ofSeconds(5));
        int pageSize = 1000;
        int cbjTotal = utUserTotalFlowService.cbjCountByCreateTimeBetween(timeFrom, toEpochMilli(timeTo));
        int cbjTotalPage = computeTotalPage(cbjTotal);
        for (int pageNumber = 0; pageNumber <= cbjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtUserTotalFlow> cbjUtUserTotalFlows = utUserTotalFlowService.cbjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            convertAndSend(cbjUtUserTotalFlows, Platform.CHEBIANJIE);
            Instant end = Instant.now();
            log.info("用户余额清洗-车便捷流水增量总页数:{},第：{}页,总用时：{} s", cbjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        DataSynTime chjDataSynTime = dataSynTimeService.findBySynType(7);
        Long chjTimeFrom = chjDataSynTime.getLastTime();
        int chjTotal = utUserTotalFlowService.chjCountByCreateTimeBetween(chjTimeFrom, toEpochMilli(timeTo));
        int chjTotalPage = computeTotalPage(chjTotal);
        for (int pageNumber = 0; pageNumber <= chjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtUserTotalFlow> chjUtUserTotalFlows = utUserTotalFlowService.chjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
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

    private Long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
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
}
