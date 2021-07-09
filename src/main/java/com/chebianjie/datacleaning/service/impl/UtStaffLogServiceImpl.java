package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.config.DataCleanConfiguration;
import com.chebianjie.datacleaning.constants.RabbitMqConstants;
import com.chebianjie.datacleaning.domain.UtStaffLog;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.dto.StaffLogMessage;
import com.chebianjie.datacleaning.repository.UtStaffLogRepository;
import com.chebianjie.datacleaning.service.UtStaffLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

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

    @Override
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public void updateCbjConsumerAccount(Long id, String consumerAccount) {
        utStaffLogRepository.updateConsumerAccount(id, consumerAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public void updateChjConsumerAccount(Long id, String consumerAccount) {
        utStaffLogRepository.updateConsumerAccount(id, consumerAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public void firstCbjClean() {
        int total = utStaffLogRepository.countByCreatetimeLessThanEqual(dataCleanConfiguration.getStaffLogTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getStaffLogStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtStaffLog> staffLogs = utStaffLogRepository.findAllByCreateTimePage(dataCleanConfiguration.getStaffLogTime(), pageNumber * pageSize, pageSize);
            convertAndSend(staffLogs, Platform.CHEBIANJIE);
            Instant end = Instant.now();
            log.info("车便捷员工业绩总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车便捷员工业绩总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public void firstChjClean() {
        int total = utStaffLogRepository.countByCreatetimeLessThanEqual(dataCleanConfiguration.getStaffLogTime());
        int totalPage = computeTotalPage(total);
        int pageSize = 1000;
        Instant totalStart = Instant.now();
        for (int pageNumber = dataCleanConfiguration.getStaffLogStartPage(); pageNumber <= totalPage; pageNumber++) {
            Instant now = Instant.now();
            List<UtStaffLog> staffLogs = utStaffLogRepository.findAllByCreateTimePage(dataCleanConfiguration.getStaffLogTime(), pageNumber * pageSize, pageSize);
            convertAndSend(staffLogs, Platform.CHEHUIJIE);
            Instant end = Instant.now();
            log.info("车惠捷员工业绩总页数:{},第：{}页,总用时：{} s", totalPage, pageNumber + 1, Duration.between(now, end).toMillis()/1000);
        }
        Instant totalEnd = Instant.now();
        log.info("车惠捷员工业绩总用时：{}ms", Duration.between(totalStart, totalEnd).toMillis());
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
