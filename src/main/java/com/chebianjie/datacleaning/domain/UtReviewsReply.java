package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ut_reviews_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtReviewsReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ut_reviews表的id
     */
    @Column(name = "reviews_id")
    @ApiModelProperty("ut_reviews表的id")
    private Long reviewsId;

    /**
     * 回复内容
     */
    @Column(name = "reply")
    @ApiModelProperty("回复内容")
    private String reply;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 赠送余额
     */
    @Column(name = "give_balance")
    @ApiModelProperty("赠送余额")
    private Long giveBalance;

    /**
     * 赠送优惠券
     */
    @Column(name = "coupon_id")
    @ApiModelProperty("赠送优惠券")
    private Long couponId;

    /**
     * 用户id
     */
    @Column(name = "consumer_id")
    @ApiModelProperty("用户id")
    private Long consumerId;

    /**
     * 用户手机号
     */
    @Column(name = "jhi_account")
    @ApiModelProperty("用户手机号")
    private String account;

    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;



}
