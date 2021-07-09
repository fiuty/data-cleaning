package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@Table(name = "ut_staff_log")
public class UtStaffLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标识：2是车便捷；3是车惠捷
     */
    @ApiModelProperty(value = "标识：2是车便捷；3是车惠捷")
    @Column(name = "weid")
    private Integer weid;

    /**
     * 充值金额（分）
     */
    @Column(name = "charge_price")
    private Integer chargePrice;

    /**
     * 首次推荐人编号, 关联员工表ut_staff中的员工编号
     */
    @ApiModelProperty(value = "首次推荐人编号, 关联员工表ut_staff中的员工编号")
    @Column(name = "first_referrer_id", nullable = true)
    private Integer firstReferrerId;

    /**
     * 当前推荐人编号
     */
    @ApiModelProperty(value = "当前推荐人编号")
    @Column(name = "referrer_id", nullable = true)
    private Integer referrerId;

    /**
     * 充值给的用户id
     */
    @ApiModelProperty(value = "充值给的用户id")
    @Column(name = "consumer_id", nullable = true)
    private Long consumerId;

    /**
     * 创建时间
     */
    @Column(name = "createtime")
    private Long createtime;

    /**
     * 订单号
     */

    @ApiModelProperty(value = "订单号")
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Long payTime;

    /**
     * ic卡号
     */
    @ApiModelProperty(value = "ic卡号")
    @Column(name = "ic_outer_num")
    private String icOuterNum;

    /**
     * 订单状态（1支付成功，2待支付，3支付失败，4用户取消支付；70待完成-待验证、71待完成-已验证、73已完成 、74已取消）
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 总提成
     */
    @Column(name = "total_commission")
    private Integer totalCommission;

    /**
     * 第一位推荐人提成
     */
    @ApiModelProperty(value = "第一位推荐人提成")
    @Column(name = "first_commission")
    private Integer firstCommission;

    /**
     * 此次推荐人提成
     */
    @ApiModelProperty(value = "此次推荐人提成")
    @Column(name = "referrer_commission")
    private Integer referrerCommission;

    /**
     * 业绩提取状态（1,已提取，2，待提取）
     */
    @ApiModelProperty(value = "业绩提取状态（1,已提取，2，待提取）")
    @Column(name = "commission_status")
    private Integer commissionStatus;

    /**
     * 充值来源，目前：0未知来源充值 , 1客户充值，2员工代充，3后台充值, 4增值服务消费；
     */
    @Column(name = "paytype")
    private Integer paytype;

    @ApiModelProperty(value = "用户账号")
    @Column(name = "account")
    private String account;

    @ApiModelProperty(value = "用户昵称")
    @Column(name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "充值类型，1用户充值、2员工代充（手机号）、5卡充值、6消费卡 7增值服务")
    @Column(name = "charge_type")
    private Integer chargeType;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("员工收益")
    private Integer profit;

    @ApiModelProperty("增值服务订单号id")
    private Long serverOrderId;

    @ApiModelProperty("是否需要启动洗车机完成服务,true-需要,false-不需要")
    @Column(name = "is_start_machine")
    private Boolean startMachine;

    /**
     * 用户中心合并后用户的唯一UUID - consumer表的union_account
     */
    private String consumerUnionAccount;

}
