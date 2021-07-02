package com.chebianjie.datacleaning.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户优惠券\n@author zjl\n@Date 2019/12/03
 */
@Entity
@Table(name = "ut_coupon_user")
@Data
@ToString(exclude = {"utCoupon", })
@EqualsAndHashCode(exclude = {"utCoupon"})
@DynamicInsert
@DynamicUpdate
public class UtCouponUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 优惠券编号
     */
    @Column(name = "code")
    private String code;

    /**
     * 批次号
     */
    @Column(name = "batch_nums")
    private String batchNums;

    /**
     * 用户手机号
     */
    @Column(name = "consumer_account")
    private String consumerAccount;

    /**
     * 用户昵称
     */
    @Column(name = "consumer_nickname")
    private String consumerNickname;

    /**
     * ut_consumer表id 用户id
     */
    @Column(name = "consumer_id")
    private Long consumerId;

    

    /**
     * ut_consumer表id 来自哪个用户的分享
     */
    @Column(name = "share_consumer_id")
    private Long shareConsumerId;

    /**
     * ut_coupon表title 优惠券表名称
     */
    @Column(name = "coupon_title")
    private String couponTitle;

    /**
     * ut_coupon表id 优惠券表id
     */
    @Column(name = "coupon_id", insertable = false, updatable = false)
    private Long couponId;

    /**
     * ut_coupon表type 优惠券表类型 (1:代金券 2:满减券 3:折扣券 4:洗车券)
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 1 代金券金额  2  满减券  满多少  3 折扣券 折扣
     */
    @Column(name = "price")
    private Integer price;

    /**
     * 2  满减券  减多少
     */
    @Column(name = "reduction")
    private Integer reduction;

    /**
     * 到期后自动续期一个月(0:不续期 1:续期)
     */
    @Column(name = "renewal")
    private Integer renewal;

    /**
     * 有效天数
     */
    @Column(name = "valid_date")
    private Long validDate;

    /**
     * 领取时间
     */
    @Column(name = "received_time")
    private Long receivedTime;

    /**
     * 使用时间
     */
    @Column(name = "used_time")
    private Long usedTime;

    /**
     * 有效时间-开始
     */
    @Column(name = "effective_time_begin")
    private Long effectiveTimeBegin;

    /**
     * 有效时间-结束
     */
    @Column(name = "effective_time_end")
    private Long effectiveTimeEnd;

    /**
     * 赠送人角色。1员工，2后台管理员  3商家营销
     */
    @Column(name = "send_type")
    private Integer sendType;

    /**
     * 关联关系表id(1员工赠送 ut_coupon_staff表id  2后台管理员   3商家营销 ut_coupon_site_configure表id  )
     */
    @Column(name = "send_relationship_id")
    private Long sendRelationshipId;

    /**
     * 赠送人id
     */
    @Column(name = "send_by")
    private Long sendBy;

    /**
     * 赠送人名称
     */
    @Column(name = "send_name")
    private String sendName;

    /**
     * 消费项目类型   1已经赠送，2:洗车订单,3:增值服务
     */
    @Column(name = "consume_type")
    private Integer consumeType;

    /**
     * 消费订单号
     */
    @Column(name = "consume_order_num")
    private String consumeOrderNum;

    /**
     * 消费订单id
     */
    @Column(name = "consume_order_id")
    private Long consumeOrderId;

    /**
     * 消费订单总金额
     */
    @Column(name = "consume_total_amount")
    private Long consumeTotalAmount;

    /**
     * 消费订单使用代金券金额
     */
    @Column(name = "consume_coupon_amount")
    private Long consumeCouponAmount;

    /**
     * 消费订单促成交易金额=消费订单总金额-消费订单使用代金券金额
     */
    @Column(name = "consume_transaction_amount")
    private Long consumeTransactionAmount;

    /**
     * 优惠券使用状态：1未使用，2已使用，3已经赠送，4已过期；
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建人id
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 创建人类型 1员工，2后台管理员 3 用户
     */
    @Column(name = "create_type")
    private Integer createType;

    /**
     * 创建人登陆账号
     */
    @Column(name = "create_account")
    private String createAccount;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private Long createdDate;

    /**
     * 更新人id
     */
    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    /**
     * 更新人类型 1员工，2后台管理员 3 用户
     */
    @Column(name = "last_modified_type")
    private Integer lastModifiedType;

    /**
     * 更新人登陆账号
     */
    @Column(name = "last_modified_account")
    private String lastModifiedAccount;

    /**
     * 更新时间
     */
    @Column(name = "last_modified_date")
    private Long lastModifiedDate;

    /**
     * 类型 1:优惠券营销 2:优惠券礼包营销 3:充值赠送营销 4:油站送券营销
     */
    @Column(name = "site_configure_type")
    private Integer siteConfigureType;

    @JoinColumn(name = "coupon_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private UtCoupon utCoupon;

    /**
     * 适用业务：0-全部、1-洗车、2-车服
     */
    private Integer business;

}
