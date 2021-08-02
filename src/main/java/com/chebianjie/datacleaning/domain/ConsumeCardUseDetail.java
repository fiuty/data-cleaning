package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhengdayue
 */
@Data
@Table
@Entity
public class ConsumeCardUseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    private Long consumerId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 机器订单号
     */
    private String equipOrder;

    /**
     * 订单金额
     */
    private Integer orderPrice;

    /**
     * 类型(1:代金券 2:满减券 3:折扣券  4:洗车券)
     */
    private Integer couponType;

    /**
     * 劵id
     */
    private Integer couponId;

    /**
     * 消费的劵金额
     */
    private Integer couponPrice;

    /**
     * 消费类型:1-洗车,2-商城,3-大客户消费类型,4-单次消费,5-中石化优惠券,6-消费卡消费
     */
    private Integer consumeType;

    /**
     * 机器号
     */
    private String machineNum;

    /**
     * 机器类型 1：洗车 2：吸尘
     */
    private Integer machineType;

    /**
     * 订单时间
     */
    private Long createTime;

    /**
     * 订单服务器时间
     */
    private LocalDateTime serverTime;

    /**
     * 站点id
     */
    private Long siteId;

    /**
     * 订单进行状态，0 未完成 -1 已取消 1已完成 2 已结算
     */
    private Integer status;

    /**
     * 用户手机号
     */
    private String tel;

    /**
     * 洗车模式  0：自助洗车  1：全自动标准洗车 2：全自动增值洗车
     */
    private Integer automaticType;

    /**
     * 消费卡id
     */
    private Long consumeCardId;

    /**
     * 用户已购消费卡id
     */
    private Long userConsumeCardId;


    /**
     * 用户唯一标识
     */
    private String consumerUnionAccount;



}
