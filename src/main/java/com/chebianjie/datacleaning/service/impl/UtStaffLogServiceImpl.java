package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.config.DataCleanConfiguration;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.DataSynTime;
import com.chebianjie.datacleaning.domain.UtStaffLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.dto.StaffLogMessage;
import com.chebianjie.datacleaning.repository.DataSynTimeRepository;
import com.chebianjie.datacleaning.repository.UtStaffLogRepository;
import com.chebianjie.datacleaning.service.DataSynTimeService;
import com.chebianjie.datacleaning.service.UtStaffLogService;
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
 * @date: 2021-07-09
 */
@Service
@Slf4j
public class UtStaffLogServiceImpl implements UtStaffLogService {

    @Autowired
    private UtStaffLogRepository utStaffLogRepository;

    @Autowired
    private DataCleanConfiguration dataCleanConfiguration;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DataSynTimeRepository dataSynTimeRepository;

    @Autowired
    private DataSynTimeService dataSynTimeService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public void updateCbjConsumerAccount(Long id, String consumerAccount) {
        utStaffLogRepository.updateConsumerAccount(id, consumerAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public void updateChjConsumerAccount(Long id, String consumerAccount) {
        utStaffLogRepository.updateConsumerAccount(id, consumerAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void firstCbjClean() {
        int total = utStaffLogRepository.countByCreatetimeLessThanEqual(dataCleanConfiguration.getStaffLogTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getStaffLogStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtStaffLog> staffLogs = utStaffLogRepository.findAllByCreateTimePage(dataCleanConfiguration.getStaffLogTime(), pageNumber * pageSize, pageSize);
            List<UtStaffLog> filterLogs = staffLogs.stream().filter(log -> log.getConsumerId() != null).collect(Collectors.toList());
            convertAndSend(filterLogs, Platform.CHEBIANJIE);
            Instant end = Instant.now();
            log.info("车便捷员工业绩总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车便捷员工业绩总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void firstChjClean() {
        int total = utStaffLogRepository.countByCreatetimeLessThanEqual(dataCleanConfiguration.getStaffLogTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getStaffLogStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtStaffLog> staffLogs = utStaffLogRepository.findAllByCreateTimePage(dataCleanConfiguration.getStaffLogTime(), pageNumber * pageSize, pageSize);
            List<UtStaffLog> filterLogs = staffLogs.stream().filter(log -> log.getConsumerId() != null).collect(Collectors.toList());
            convertAndSend(filterLogs, Platform.CHEHUIJIE);
            Instant end = Instant.now();
            log.info("车惠捷员工业绩总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车惠捷员工业绩总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void utstaffLogJob() {
        DataSynTime dataSynTime = dataSynTimeRepository.findBySynType(2);
        Long timeFrom = dataSynTime.getLastTime();
        LocalDateTime timeTo = LocalDateTime.now().minus(Duration.ofSeconds(5));
        int pageSize = 1000;
        int cbjTotal = this.cbjCountByCreateTimeBetween(timeFrom, toEpochMilli(timeTo));
        int cbjTotalPage = computeTotalPage(cbjTotal);
        for (int pageNumber = 0; pageNumber <= cbjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtStaffLog> cbjStaffLogs = this.cbjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            List<UtStaffLog> filterLogs = cbjStaffLogs.stream().filter(log -> log.getConsumerId() != null).collect(Collectors.toList());
            convertAndSend(filterLogs, Platform.CHEBIANJIE);
            Instant end = Instant.now();
            log.info("车便捷员工业绩总页数:{},第：{}页,总用时：{} s", cbjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        int chjTotal = this.chjCountByCreateTimeBetween(timeFrom, toEpochMilli(timeTo));
        int chjTotalPage = computeTotalPage(chjTotal);
        for (int pageNumber = 0; pageNumber <= chjTotalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtStaffLog> chjStaffLogs = this.chjFindAllByCreateTimeBetween(timeFrom, toEpochMilli(timeTo), pageNumber, pageSize);
            List<UtStaffLog> filterLogs = chjStaffLogs.stream().filter(log -> log.getConsumerId() != null).collect(Collectors.toList());
            convertAndSend(filterLogs, Platform.CHEHUIJIE);
            Instant end = Instant.now();
            log.info("车惠捷员工业绩总页数:{},第：{}页,总用时：{} s", chjTotalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        dataSynTime.setLastTime(toEpochMilli(timeTo));
        dataSynTimeService.updateDataSynTime(dataSynTime);
        log.info("同步增量,车便捷员工业绩增量cbjTotal：{}，车惠捷流水增量chjTotal：{}，起始时间timeFrom：{}，截止时间timeTo：{}", cbjTotal, chjTotal, timeFrom, timeTo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public int cbjCountByCreateTimeBetween(Long timeFrom, Long timeTo) {
        return utStaffLogRepository.countByCreatetimeBetween(timeFrom,timeTo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public int chjCountByCreateTimeBetween(Long timeFrom, Long timeTo) {
        return utStaffLogRepository.countByCreatetimeBetween(timeFrom,timeTo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public List<UtStaffLog> cbjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize) {
        return utStaffLogRepository.findAllByCreatetimeBetweenPage(timeFrom, timeTo, pageNumber * pageSize, pageSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public List<UtStaffLog> chjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize) {
        return utStaffLogRepository.findAllByCreatetimeBetweenPage(timeFrom, timeTo, pageNumber * pageSize, pageSize);
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


    private void convertAndSend(List<UtStaffLog> staffLogs, Platform platform) {
        staffLogs.forEach(staffLog -> {
            StaffLogMessage staffLogMessage = new StaffLogMessage();
            staffLogMessage.setId(staffLog.getId());
            staffLogMessage.setPlatform(platform);
            staffLogMessage.setConsumerId(staffLog.getConsumerId());
            rabbitTemplate.convertAndSend(RabbitMqConstants.DATA_CLEAN_FIRST_STAFF_LOG_EXCHANGE, RabbitMqConstants.DATA_CLEAN_FIRST_STAFF_LOG_ROUTING_KEY,
                    staffLogMessage);
        });
    }
}
