package com.chebianjie.datacleaning.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Data
@Entity
@Table
public class DataSynTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 最后同步时间
     */
    private Long lastTime;

    /**
     * 同步类型:1-车便捷-用户流水增量，3-车惠捷-用户流水增量，2-员工业绩，6-车便捷-流水增量，用户余额清洗，7-车惠捷-流水增量，用户余额清洗
     */
    private Integer synType;
}
