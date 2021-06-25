package com.chebianjie.datacleaning.domain;


import com.chebianjie.datacleaning.domain.enums.BalanceType;
import lombok.Data;

import javax.persistence.*;

/**
 * 消费者余额
 *
 * @author 许泽坤
 * @create 2021-06-03 9:53
 */
@Data
@Table
@Entity
public class ConsumerBalance {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 消费者ID
     */
    private Long consumerId;

    /**
     * 消费者唯一账号
     */
    private String unionAccount;

    /**
     * 余额
     */
    private Integer value;
}
