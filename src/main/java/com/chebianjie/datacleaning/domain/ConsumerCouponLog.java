package com.chebianjie.datacleaning.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "consumer_coupon_log")
@Data
public class ConsumerCouponLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ut_coupon_user表主键
     */
    private long utCouponUserId;

    /**
     * 类型: 类型 1:车便捷 2:车惠捷
     */
    private int type;

    /**
     * 状态 0: 失败 1:成功
     */
    private int status;
}
