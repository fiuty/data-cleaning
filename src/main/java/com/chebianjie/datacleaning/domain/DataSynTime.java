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
     * 同步类型:1-用户流水，2-员工业绩
     */
    private Integer synType;
}
