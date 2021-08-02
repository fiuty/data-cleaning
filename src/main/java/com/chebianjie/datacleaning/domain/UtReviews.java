package com.chebianjie.datacleaning.domain;

import com.chebianjie.common.core.enums.WashMachineClassModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "ut_reviews")
@Data
public class UtReviews implements Serializable {

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
     * 订单id
     */
    @NotNull
    @Column(name = "consump_id")
    @ApiModelProperty("订单id")
    private Long consumpId;

    /**
     * 订单号
     */
    @NotNull
    @Column(name = "order_num")
    @ApiModelProperty("订单号")
    private String orderNum;

    /**
     * 评论
     */
    @NotNull
    @Column(name = "reviews")
    @ApiModelProperty("评论")
    private String reviews;

    /**
     * 星级
     */
    @NotNull
    @Column(name = "star_class")
    @ApiModelProperty("星级")
    private Integer starClass;

    /**
     * 赠送优惠券id
     */
    @Column(name = "coupon_id")
    @ApiModelProperty("赠送优惠券id")
    private Long couponId;

    /**
     * 创建时间
     */
    @NotNull
    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Long createTime;

    @Column(name = "give_balance")
    @ApiModelProperty("赠送余额")
    private Long giveBalance;

    @Column(name = "remark")
    @ApiModelProperty("评论")
    private String remark;


    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;



}
