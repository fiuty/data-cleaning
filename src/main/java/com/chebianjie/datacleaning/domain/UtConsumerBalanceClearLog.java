package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@ApiModel(description = "退款记录表")
@Entity
@Table(name = "ut_consumer_balance_clear_log")
public class UtConsumerBalanceClearLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id(用户表ut_consumer id 作为外键)
     */
    @NotNull
    @ApiModelProperty(value = "用户id(用户表ut_consumer id 作为外键)", required = true)
    @Column(name = "jhi_uid", nullable = false)
    private Long uid;

    /**
     * 用户账号（手机号）<br>
     * 表字段：account
     */
    @ApiModelProperty(value = "用户账号（手机号）", required = true)
    @Column(name = "account", nullable = false)
    private String account;

    /**
     * 用户openid<br>
     * 表字段：openid
     */
    @ApiModelProperty(value = "用户openid", required = true)
    @Column(name = "openid", nullable = false)
    private String openid;

    /**
     * 真实姓名<br>
     * 表字段：real_name
     */
    @ApiModelProperty(value = "真实姓名", required = true)
    @Column(name = "real_name", nullable = false)
    private String realName;

    /**
     * 处理状态。-1：已拒绝，0：未处理，1：已同意，2：用户已撤销<br>
     * 表字段：deal_status
     */
    @ApiModelProperty(value = "处理状态。-2：用户已撤销，-1：已拒绝，0：未处理，1：已同意", required = true)
    @Column(name = "deal_status", nullable = false)
    private Integer dealStatus;

    /**
     * 清空前余额（以分为单位，以后主要使用这个）
     */
    @NotNull
    @ApiModelProperty(value = "清空前余额（以分为单位，以后主要使用这个）", required = true)
    @Column(name = "balance", nullable = false)
    private Integer balance;

    @ApiModelProperty(value = "退赠送余额，大于等于0，小于原赠送金额（默认，所有赠送金额，不扣时填0）", required = true)
    @Column(name = "give_balance", nullable = false)
    private Integer giveBalance;

    /**
     * 审批备注<br>
     * 表字段：approval_remark
     */
    @Size(max = 255)
    @ApiModelProperty(value = "审批备注", required = true)
    @Column(name = "approval_remark",  length = 255)
    private String approvalRemark;

    /**
     * 审批时间<br>
     * 表字段：approval_time
     */
    @ApiModelProperty(value = "审批时间", required = true)
    @Column(name = "approval_time", nullable = false)
    private Long approvalTime;
    /**
     * 操作人id(登陆人 ut_users id 作为外键)
     */
    @ApiModelProperty(value = "操作人id(登陆人 ut_users id 作为外键)", required = true)
    @Column(name = "cid", nullable = false)
    private Long cid;

    /**
     * 创建时间（申请时间）<br>
     */
    @NotNull
    @ApiModelProperty(value = "创建时间（申请时间）", required = true)
    @Column(name = "create_time", nullable = false)
    private Long createTime;
    /**
     * 到账状态。0：未处理，1：处理中，2：已到账，-1：到账失败,-2：用户已撤销 <br>
     * 表字段：status
     */
    @ApiModelProperty(value = "到账状态。0：未处理，1：处理中，2：已到账，-1：到账失败,-2：用户已撤销,-3:拒绝退款", required = true)
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 微信反馈<br>
     * 表字段：result
     */
    @ApiModelProperty(value = "微信反馈", required = true)
    @Column(name = "result", nullable = false)
    private String result;

    /**
     * 到账时间<br>
     * 表字段：received_time
     */
    @ApiModelProperty(value = "到账时间", required = true)
    @Column(name = "received_time", nullable = false)
    private Long receivedTime;

    /**
     * 商户订单号（唯一）<br>
     * 表字段：trade_no
     */
    @ApiModelProperty(value = "商户订单号（唯一）", required = true)
    @Column(name = "trade_no", nullable = false)
    private String tradeNo;


    @ApiModelProperty(value = "操作人角色类型。1-后台管理员，2-员工", required = true)
    @Column(name = "role", nullable = false)
    private Integer role;

    @ApiModelProperty(value = "操作人用户名", required = true)
    @Column(name = "c_name", nullable = false)
    private String cName;

    @ApiModelProperty(value = "抵扣积分（默认，所有积分，不扣积分时填0）")
    @Column(name = "integral", nullable = false)
    private Integer integral;
    /**
     * 退款原因
     */
    @Size(max = 255)
    @ApiModelProperty(value = "退款原因")
    @Column(name = "remark", length = 255)
    private String remark;


    @ApiModelProperty(value = "订单号")
    @Column(name = "order_num")
    private String orderNum;

    @ApiModelProperty(value = "省名称")
    @Column(name = "province")
    private String province;

    @ApiModelProperty(value = "市名称")
    @Column(name = "city")
    private String city;

    @ApiModelProperty(value = "用户绑定的站点id")
    @Column(name = "stair_site_id")
    private Long stairSiteId;

    @ApiModelProperty(value = "用户绑定的站点名称")
    @Column(name = "stair_site_name")
    private String stairSiteName;


    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;



}
