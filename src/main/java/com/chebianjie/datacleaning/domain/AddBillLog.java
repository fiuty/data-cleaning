package com.chebianjie.datacleaning.domain;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

import javax.persistence.*;

/**
 * 监听流水增量log
 * @author zhengdayue
 * @date: 2021-07-06
 */
@Table
@Entity
@Data
public class AddBillLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 旧流水id
     */
    private Long utUserTotalFlowId;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    /**
     * 0-失败、1-成功
     */
    private Integer status;

    /**
     * 流水json,失败保存json
     */
    private String json;

    public AddBillLog(Long utUserTotalFlowId, Platform platform, Integer status, String json) {
        this.utUserTotalFlowId = utUserTotalFlowId;
        this.platform = platform;
        this.status = status;
        this.json = json;
    }

    public AddBillLog(){}

}
