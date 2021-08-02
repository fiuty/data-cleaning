package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@ApiModel(description = "用户微信解绑记录表")
@Entity
@Data
@Table(name = "ut_consumer_log")
public class UtConsumerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 与ut_consumer.uid进行关联（暂时继续保留使用 ，后期建议改为与id关联并放弃该字段）
     */
    @NotNull
    @ApiModelProperty(value = "与ut_consumer.uid进行关联（暂时继续保留使用 ，后期建议改为与id关联并放弃该字段）", required = true)
    @Column(name = "jhi_uid", nullable = false)
    private Long uid;

    /**
     * 用户解绑前微信名称
     */
    @NotNull
    @ApiModelProperty(value = "用户解绑前微信名称", required = true)
    @Column(name = "nickname", nullable = false)
    private String nickname;

    /**
     * 用户解绑前账号
     */
    @NotNull
    @ApiModelProperty(value = "用户解绑前账号", required = true)
    @Column(name = "jhi_account", nullable = false)
    private String account;

    /**
     * 用户密码
     */
    @NotNull
    @ApiModelProperty(value = "用户密码", required = true)
    @Column(name = "pwd", nullable = false)
    private String pwd;

    /**
     * 解绑前的账号余额（以分为单位，以后主要使用这个）
     */
    @NotNull
    @ApiModelProperty(value = "解绑前的账号余额（以分为单位，以后主要使用这个）", required = true)
    @Column(name = "balance", nullable = false)
    private Integer balance;

    /**
     * 解绑时间
     */
    @NotNull
    @ApiModelProperty(value = "解绑时间", required = true)
    @Column(name = "create_time", nullable = false)
    private Long createTime;


    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;






}
