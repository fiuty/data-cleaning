package com.chebianjie.datacleaning.domain;


import com.chebianjie.datacleaning.domain.enums.BalanceType;
import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

import javax.persistence.*;

/**
 * 消费者账单变更详情
 *
 * @author 许泽坤
 * @create 2021-06-03 10:24
 */
@Data
@Table
@Entity
public class ConsumerBillChangeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 平台
     */
    @Enumerated(EnumType.STRING)
    private Platform platform;

    /**
     * 账单标识
     */
    private String billIdentify;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 余额类型
     */
    @Enumerated(EnumType.STRING)
    private BalanceType balanceType;

    /**
     * 变更前的值
     */
    private Integer preChangeValue;

    /**
     * 变更值
     */
    private Integer changeValue;

    /**
     * 变更后的值
     */
    private Integer afterChangeValue;

}
