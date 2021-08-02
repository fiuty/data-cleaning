package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "ut_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @NotNull
    @Column(name = "consumer_id")
    @ApiModelProperty("用户id")
    private Long consumerId;

    /**
     * 账户名(即用户手机号)
     */
    @NotNull
    @Column(name = "jhi_account")
    @ApiModelProperty("账户名(即用户手机号)")
    private String account;

    /**
     * 意见
     */
    @Column(name = "opinion")
    @ApiModelProperty("意见")
    private String opinion;

    /**
     * 是否采用,1采用 2未采用
     */
    @Column(name = "is_adopt")
    @ApiModelProperty("是否采用,1采用 2未采用")
    private Integer is_adopt;

    /**
     * 采用后发券id
     */
    @Column(name = "coupon_id")
    @ApiModelProperty("采用后发券id")
    private Long couponId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 备注
     */
    @Column(name = "remark")
    @ApiModelProperty("备注")
    private String remark;

    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;






}
