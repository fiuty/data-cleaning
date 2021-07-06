package com.chebianjie.datacleaning.domain.order;

import com.chebianjie.datacleaning.domain.enums.OrderSource;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: wpan
 * @Description: 吸尘器订单数据
 * @Version :0.0.1
 * @Date: 2020-11-11
 */
@Data
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class DushOrder extends AbstractEntity implements Serializable {




    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 平台订单编号 */
    private String orderNum;
    /** 机器订单编号 */
    private String equipOrder;
    /** 登录方式 1 刷卡登录；2 输入账号登录；3 车牌登录； 4 条形码； 5扫二维码(代金券) */
    @Column(name = "login_type", nullable = false)
    private Integer type;
    /** IC卡号或条形码编号 */
    private String icAccount;
    /** 卡类型：8,正式卡，5，员工卡，6,赠送卡 */
    private Integer icType;
    /** 优惠券类型(1:代金券 2:满减券 3:折扣券  4:洗车券 5:中石化电子券) */
    private Integer couponType;
    /** 优惠券id */
    private Long couponId;
    /** 优惠券名称 */
    private String couponName;
    /** 优惠券金额 */
    private Integer couponPrice;
    /** 消费类型:1-洗车,2-商城,3-大客户消费类型,4-单次消费,5-中石化优惠券,6-年卡消费,7-增值服务 */
    private Integer consumeType;
    /** 站点id */
    private Long siteId;
    /** 站点名称 */
    private String siteName;
    /** 设备id */
    private Long machineId;
    /** 设备号 */
    private String machineNum;
    /** 消费来源。1-公众号，2-app，3-小程序，4-后台插入,5-异常订单,6-数据导入 */
    private Integer dataFrom;
    /** 订单进行状态，0 未完成 -1 已取消 1已完成  2 已结算 3 未启动机器 4 超时取消 */
    private Integer status;
    /** 消费者id */
    private Long consumerId;
    /** 消费者手机号 */
    private String tel;
    /** 消费者账号*/
    private String consumerAccount;
    /** 红包余额比例，默认0 */
    private Integer consumerGiftBalanceRatio;
    /** 用户余额比例，默认100 */
    private Integer consumerBalanceRatio;
    /** 赠送余额消费金额 */
    private Integer giveBalance;
    /** 余额消费金额 */
    private Integer balance;
    /** 支付金额 */
    private Integer realPrice;
    /** 订单总金额 */
    private Integer orderPrice;
    /** 停止原因( 正常停止   超时取消) */
    private String stopReason;
    /** 省id */
    private Long provinceId;
    /** 省名称 */
    private String province;
    /** 市id */
    private Long cityId;
    /** 市名称 */
    private String city;
    /** 区id */
    private Long areaId;
    /** 区名称 */
    private String area;
    /** 大客户运营商agentId */
    private Long agentId;
    /** 单次消费微信流水号 */
    private String transactionId;
    /** 用户已购消费卡id */
    private Long userConsumeCardId;
    /** 吸尘时间 */
    private Integer dushTime;
    /** 吸尘单价 */
    private Integer dushPrice;
    /** 吸尘消费金额 */
    private Integer dushBalance;
    /** 洗手时间 */
    private Integer washTime;
    /** 洗手单价 */
    private Integer washPrice;
    /** 洗手消费金额 */
    private Integer washBalance;

    /** 消毒时间**/
    private Integer disinfectTime;
    /** 消毒单价**/
    private Integer disinfectPrice;
    /** 消毒消费金额 **/
    private Integer disinfectBalance;


    /** 加收比例 */
    private Integer addedPrice;
    /** 加收时段 */
    private String addedPeriod;
    /** 总时间 */
    private Integer totalTime;
    /** 开始时间 */
    private Long startTime;
    /** 结束时间 */
    private Long endTime;
    /** 支付时间 */
    private Long payTime;
    /** 消费时间 */
    private Long createTime;
    /** 是否评价 0-待评价，1-已评价 */
    @Column(name = "is_wash_comment")
    private Boolean washComment;
    /** 乐观锁 */
    @Version
    private Integer revision;

    /**
     * 是否为工厂订单,true-工厂订单,false-不是工厂订单
     */
    @Column(name = "is_factory")
    private Boolean factory;

    /**
     * 订单来源
     */
    @Enumerated(EnumType.STRING)
    private OrderSource orderSource;

    private String platform;

    /**
     * 业务id,与consumeType消费类型结合
     */
    private Long businessId;

}

