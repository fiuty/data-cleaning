package com.chebianjie.datacleaning.domain;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

import javax.persistence.*;

/**
 * 记录流水清洗
 * @author zhengdayue
 * @date: 2021-07-07
 */
@Table
@Entity
@Data
public class FlowLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户旧流水id
     */
    private Long utUserTotalFlowId;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    /**
     * 状态：0-失败。1-成功
     */
    private Integer status;
}
