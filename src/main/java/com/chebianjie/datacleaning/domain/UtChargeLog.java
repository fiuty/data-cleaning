package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 充值记录
 * @author xiaoze
 * @Date 2018/11/16
 */
@ApiModel(description = "充值记录 @author xiaoze @Date 2018/11/16")
@Entity
@Table(name = "ut_charge_log")
@Data
public class UtChargeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标识：2是车便捷；3是车惠捷
     */
    @ApiModelProperty(value = "标识：2是车便捷；3是车惠捷", required = true)
    @Column(name = "weid", nullable = false)
    private Integer weid;

    /**
     * 充值人uid(旧系统的ims_jy_cbj2_user.uid，新系统的ut_admin.uid)
     */
    @ApiModelProperty(value = "充值人uid(旧系统的ims_jy_cbj2_user.uid，新系统的ut_admin.uid)")
    @Column(name = "payer_id")
    private Long payerId;

    /**
     * 获取人uid(旧系统的ims_jy_cbj2_user.uid，新系统的ut_consumer.uid)，为了兼容旧系统而保留的uid
     */
    @ApiModelProperty(value = "获取人uid(旧系统的ims_jy_cbj2_user.uid，新系统的ut_consumer.uid)，为了兼容旧系统而保留的uid")
    @Column(name = "consumer_id")
    private Long consumerId;


    /** 消费者账号 */
    @Transient
    private String consumerAccount;

    /**
     * 订单号（唯一）
     */
    @ApiModelProperty(value = "订单号（不为空时值唯一）")
    @Column(name = "ordernum")
    private String ordernum;

    /**
     * 微信返回的订单号
     */
    @ApiModelProperty(value = "微信返回的订单号")
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 原余额（新系统，分为单位）
     */
    @ApiModelProperty(value = "原余额（新系统，分为单位）", required = true)
    @Column(name = "old_balance", nullable = false)
    private Integer oldBalance;

    /**
     * 充值选项id（充值时有多个可选金额，每个选项对应一个编号）
     */
    @ApiModelProperty(value = "充值选项id（充值时有多个可选金额，每个选项对应一个编号）")
    @Column(name = "price_id")
    private Integer priceId;

    /**
     * 充值金额（以分为单位）
     */
    @ApiModelProperty(value = "充值金额（以分为单位）")
    @Column(name = "charge_price")
    private Integer chargePrice;

    /**
     * 赠送的积分
     */
    @ApiModelProperty(value = "赠送的积分")
    @Column(name = "interal")
    private Integer interal;

    /**
     * 送多少（搞活动的时候充值返现）
     */
    @ApiModelProperty(value = "送多少（搞活动的时候充值返现）")
    @Column(name = "send_price")
    private Integer sendPrice;

    /**
     * 详见：ut_paytype ,支付的类型，支付类型。1-微信支付，2-支付宝支付，3-银联支付，4-余额支付，5-后台充值，6-积分支付，7-代金券支付，8-车币支付
     */
    @ApiModelProperty(value = "支付类型。1-微信支付，2-支付宝支付，3-银联支付，4-余额支付，5-后台充值，6-积分支付，7-代金券支付，8-车币支付", required = true)
    @Column(name = "pay_type", nullable = false)
    private Integer payType;
    /**
     /**
     * 充值类型，1用户充值、2是员工（代用户）充值、3是后台充值、4注册赠送、5员工代充（ic卡）、6大客户充值、7单次消费充值、8年卡充值、9余额转移 10增值服务
     */
    @ApiModelProperty(value = "充值类型，1用户充值、2是员工（代用户）充值、3是后台充值、4注册赠送，5员工代充（ic卡），6大客户充值、7单次消费充值、8年卡充值 10增值服务")
    @Column(name = "charge_type")
    private Integer chargeType;


    /**
     * 订单状态（1支付成功，2待支付，3支付失败，4用户取消支付）
     */
    @ApiModelProperty(value = "订单状态（1支付成功，2待支付，3支付失败，4用户取消支付，5已完成）")
    @Column(name = "status")
    private Integer status;

    /**
     * 订单生成时间（用时间戳）
     */
    @ApiModelProperty(value = "订单生成时间（用时间戳）")
    @Column(name = "create_time")
    private Long createTime;

    @Transient
    private Long createTimeStart;
    @Transient
    private Long createTimeEnd;
    @Transient
    private Long recommenderId;
    /**
     * ic卡号（存储的是ic卡的外卡号，关联表ut_icard的，ic卡也可以充值，无账号，无需关注公众号）
     */
    @ApiModelProperty(value = "ic卡号（存储的是ic卡的外卡号，关联表ut_icard的，ic卡也可以充值，无账号，无需关注公众号）")
    @Column(name = "icidouter")
    private String icidouter;


    /**
     * 卡类型，8、9洗车卡（正式卡），6活动卡，5员工卡
     */
    @ApiModelProperty(value = "卡类型，8、9洗车卡（正式卡），6活动卡，5员工卡")
    @Column(name = "icard_type")
    private Integer icardType;

    @ApiModelProperty(value = "订单来源。1-公众号，2-app，3-小程序")
    @Column(name = "data_from")
    private Integer dataFrom;

    @ApiModelProperty(value = "小程序表单id")
    @Column(name = "form_id")
    private String formId;

    @ApiModelProperty(value = "小程序支付prepay_id")
    @Column(name = "prepay_id")
    private String prepayId;

    @ApiModelProperty(value = "绑定站点id")
    @Column(name = "stair_site_id")
    private Long stairSiteId;

    @ApiModelProperty(value = "备注")
    @Column(name = "remark")
    private String  remark;

    @Transient
    @ApiModelProperty(value = "充值套餐对应充值金额和赠送金额主键")
    private Long chargeSetPricesId;

    @Column(name = "agent_id")
    private Long agentId;

}
