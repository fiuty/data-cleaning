package com.chebianjie.datacleaning.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhengdayue
 * @date: 2021-06-28
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "dataclean")
public class DataCleanConfiguration {

    /**
     * 流水清洗用户时间节点
     */
    private String flowConsumerTime;

    /**
     * 流水清洗用户分页点
     */
    private int flwoConsumerStartPage;

    /**
     * 员工业绩清洗时间节点
     */
    private Long staffLogTime;

    /**
     * 流水业绩分页点
     */
    private int staffLogStartPage;

    /**
     * 用户清洗时间节点
     */
    private Long consumerTime;

    /**
     * 用户清洗分页点
     */
    private int consumerStartPage;

    public LocalDateTime getFlowConsumerTime() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(flowConsumerTime, df);
    }

}
