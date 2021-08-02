package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "wash_order_detail")
public class WashOrderDetail implements Serializable {


    private static final long serialVersionUID = 8614876093978254568L;

    /**
     * id:
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * consumerGiftBalanceRatio:红包余额比例，默认0
     */
    private Integer consumerGiftBalanceRatio;

    /**
     * consumerBalanceRatio:用户余额比例，默认100
     */
    private Integer consumerBalanceRatio;

    /**
     * giveBalance:赠送余额消费
     */
    private Integer giveBalance;

    /**
     * balance:真实余额消费
     */
    private Integer balance;

    /**
     * provinceId:省id
     */
    private Integer provinceId;

    /**
     * province:省
     */
    private String province;

    /**
     * cityId:市id
     */
    private Integer cityId;

    /**
     * city:市
     */
    private String city;

    /**
     * areaId:区id
     */
    private Integer areaId;

    /**
     * area:区
     */
    private String area;

    /**
     * siteId:站点id
     */
    private Long siteId;

    /**
     * machineNum:机器号
     */
    private String machineNum;

    /**
     * machineType:机器类型 1：洗车 2：吸尘
     */
    private Integer machineType;

    /**
     * consumerId:用户ID
     */
    private Long consumerId;

    /**
     * loginType:1 刷卡登录；2 输入账号登录；3 车牌登录； 4 条形码； 5扫二维码(代金券)
     */
    private Integer loginType;

    /**
     * icAccount:IC卡号或条形码编号
     */
    private String icAccount;

    /**
     * icType:8 or 9.洗车卡，5.员工卡，6.活动卡
     */
    private Integer icType;

    /**
     * orderId:平台生成的订单号
     */
    private Long orderId;

    /**
     * orderNum:平台生成的订单号
     */
    private String orderNum;

    /**
     * equipOrder:机器传给我们的订单号
     */
    private String equipOrder;

    /**
     * orderPrice:订单总金额（如果有优惠券就是扣除优惠券的订单总金额）
     */
    private Integer orderPrice;

    /**
     * couponType:优惠券类型(1:代金券 2:满减券 3:折扣券  4:洗车券)
     */
    private Integer couponType;

    /**
     * couponId:优惠劵id
     */
    private Integer couponId;

    /**
     * couponPrice:消费的优惠劵金额
     */
    private Integer couponPrice;

    /**
     * createTime:创建时间
     */
    private Long createTime;

    /**
     * consumeType:预留
     */
    private Integer consumeType;


    /**
     * isStatistics:是否已统计  0：未统计   1：已统计
     */
    private Integer isStatistics;


    private LocalDateTime createDate = LocalDateTime.now();

    /**
     * 订单开始时间
     */
    private Long startTime;

    /**
     * 订单结束时间
     */
    private Long endTime;


    /**
     * 用户手机号
     */
    private String tel;


    /**
     * 是否平台互联
     */
    private Integer isPlatform;

    /**
     * 用户唯一标识
     */
    private String consumerUnionAccount;


}
