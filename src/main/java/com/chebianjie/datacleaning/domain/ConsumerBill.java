package com.chebianjie.datacleaning.domain;


import com.chebianjie.datacleaning.domain.enums.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 消费者账单
 *
 * @author 许泽坤
 * @create 2021-06-03 10:03
 */
@Data
@Table
@Entity
public class ConsumerBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 平台
     */
    @Enumerated(EnumType.STRING)
    private Platform platform;

    /**
     * 消费者唯一账号
     */
    private String unionAccount;

    /**
     * 账单标识
     */
    private String billIdentify;

    /**
     * 账单关联订单ID
     */
    private String orderId;

    /**
     * 账单关联订单号
     */
    private String orderNum;

    /**
     * 账单关联平台
     */
    @Enumerated(EnumType.STRING)
    private Platform orderPlatform;

    /**
     * 账单类型
     */
    @Enumerated(EnumType.STRING)
    private BillType billType;

    /**
     * 订单开始时间
     */
    private LocalDateTime orderStartTime;

    /**
     * 订单结束时间
     */
    private LocalDateTime orderEndTime;

    /**
     * 订单类型
     */
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * 订单支付类型
     */
    @Enumerated(EnumType.STRING)
    private OrderPaymentType orderPaymentType;

    /**
     * 订单是否评论
     */
    private Boolean orderCommented;

    /**
     * 账单是否显示
     */
    private Boolean display;

    /**
     * 账单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 账单备注
     */
    private String remark;

    /**
     * 版本
     */
    @Version
    private Integer version;

    /**
     * 消费地点
     */
    private String consumptionLocation;

    /**
     * 支付方式
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * 变动金额
     */
    private Integer paymentAmount;

    /**
     * 是否使用折扣
     */
    private Boolean hasDiscount;

    /**
     * 折扣类型
     */
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    /**
     * 折扣ID
     */
    private Long discountId;

    /**
     * 使用折扣平台
     */
    @Enumerated(EnumType.STRING)
    private Platform discountPlatform;

    /**
     * 折扣名称
     */
    private String discountName;

    /**
     * 折扣值
     */
    private Integer discountValue;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 用户余额变动操作人
     */
    private String operationName;

    /**
     * 微信支付单号
     */
    private String wechatnum;
}
