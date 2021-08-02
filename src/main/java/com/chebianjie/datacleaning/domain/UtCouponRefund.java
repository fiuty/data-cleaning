package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description
 * @Author
 * @Date 2020-01-02
 */

@Entity
@Table ( name ="ut_coupon_refund" )
@Data
public class UtCouponRefund implements Serializable {

	private static final long serialVersionUID =  5204644896244625309L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 用户id
	 */
   	@Column(name = "consumer_id" )
	private Long consumerId;

	/**
	 * ut_consumer_balance_clear_log表id
	 */
   	@Column(name = "clear_log_id" )
	private Long clearLogId;

	/**
	 * ut_coupon_user表的id
	 */
   	@Column(name = "coupon_user_id" )
	private Long couponUserId;

	@Column(name = "consumer_union_account")
	@ApiModelProperty("用户唯一标识")
	private String consumerUnionAccount;


}
