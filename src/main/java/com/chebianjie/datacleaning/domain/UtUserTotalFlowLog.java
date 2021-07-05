package com.chebianjie.datacleaning.domain;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Data
@Entity
@Table
public class UtUserTotalFlowLog {

    private Long id;

    /**
     * 监听流水json
     */
    private String json;

    /**
     * 平台
     */
    private Platform platform;

    /**
     * 流水id
     */
    private Long UtUserTotalFlowId;

}
