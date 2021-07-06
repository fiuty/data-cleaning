package com.chebianjie.datacleaning.domain.order;

import com.chebianjie.datacleaning.domain.enums.OrderSource;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.io.Serializable;

/**
 * 洗车业绩表、消费表
 *
 * @author xiaoze
 * @date 2018/11/16
 */
@Data
@Entity
@Table(name = "ut_consump")
@DynamicInsert
@DynamicUpdate
public class UtConsump implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标识：2是车便捷；3是车惠捷
     */
    @Column(name = "weid", nullable = false)
    private Integer weid;

    /**
     * 消费者ID（用户表consumer），如果是IC卡或者条形码消费则插入0
     */
    @Column(name = "consumer_id", nullable = false)
    private Integer consumerId;


    /** 消费者账号 */
    private String consumerAccount;

    /**
     * 传过来的sid（1.0版本的userinfo ID）
     */
    @Column(name = "sid", nullable = false)
    private String sid;

    /**
     * 1 刷卡登录；2 输入账号登录；3 车牌登录； 4 条形码； 5扫二维码(代金券)
     */
    @Column(name = "jhi_type", nullable = false)
    private Integer type;

    /**
     * IC卡号或条形码编号(如果是卡码消费)
     */
    @Column(name = "ic_account", nullable = false)
    private String icAccount;

    /**
     * type=1时有用，8,正式卡，5，员工卡，6,赠送卡；非卡登录时为0
     */
    @Column(name = "ic_type", nullable = false)
    private Integer icType;

    /**
     * 后台生成的订单号，也是一致对外显示的订单号，现统一调用交易服务里的ufn_dealnum生成
     */
    @Column(name = "order_num", nullable = false, unique = true)
    private String orderNum;

    /**
     * 站点设备传过来的订单号，不对外显示，用于和系统间关联
     */
    @Column(name = "equip_order", nullable = false, unique = true)
    private String equipOrder;

    /**
     * 消费真实金额
     */
    @Column(name = "real_price", nullable = false)
    private Integer realPrice;

    /**
     * 代金券id（要注意卡、券、条形码的区分，不能混淆），非券洗车传0
     */
    @Column(name = "coupon_id", nullable = false)
    private Integer couponId;


    /**
     * 代金券类型
     */
    @Column(name = "coupon_type", nullable = false)
    private Integer couponType;

    /**
     * 代金券消费的金额(quanid非0时该字段必填)
     */
    @Column(name = "coupon_price", nullable = false)
    private Integer couponPrice;

    /**
     * 消费类型：1、洗车;2、大客户消费类型
     */
    @Column(name = "consume_type", nullable = false)
    private Integer consumeType;

    /**
     * 清水时间
     */
    @Column(name = "water_time", nullable = false)
    private Integer waterTime;

    /**
     * 泡沫时间
     */
    @Column(name = "froth_time", nullable = false)
    private Integer frothTime;

    /**
     * 吸尘时间
     */
    @Column(name = "dush_time", nullable = false)
    private Integer dushTime;

    @Transient
    private Integer waterDushtype;

    /**
     * 洗手时间
     */
    @Column(name = "wash_time", nullable = false)
    private Integer washTime;

    /**
     * 总时间
     */
    @Column(name = "total_time", nullable = false)
    private Integer totalTime;

    /**
     * 设备号（洗车消费存在否则为0）
     */
    @Column(name = "equip_id", nullable = false)
    private String equipId;

    /**
     * 商店id（商城消费id，未来扩展预留的，暂时不用管）
     */
    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    /**
     * 消费时间
     */
    @Column(name = "create_time", nullable = false)
    private Long createTime;


    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Long startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Long endTime;


    /**
     * 绑定站点id
     */
    @Column(name = "stair_site_id")
    private Long stairSiteId;


    /**
     * 省id
     */
    @Column(name = "province_id")
    private Integer provinceId;

    /**
     * 省
     */
    @Column(name = "province")
    private String province;

    /**
     * 市id
     */
    @Column(name = "city_id")
    private Integer cityId;

    /**
     * 市
     */
    @Column(name = "city")
    private String city;


    /**
     * 区id
     */
    @Column(name = "area_id")
    private Integer areaId;


    /**
     * 区
     */
    @Column(name = "area")
    private String area;


    /**
     * 消费来源。1-公众号，2-app，3-小程序，4-后台插入 5-异常订单
     */
    @Column(name = "data_from")
    private Integer dataFrom;

    /**
     * 订单进行状态，0-进行中，1-已完成
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 用户手机号
     */
    @Column(name = "tel")
    private String tel;

    /**
     * 红包余额比例，默认0
     */
    @Column(name = "consumer_gift_balance_ratio")
    private Integer consumerGiftBalanceRatio;

    /**
     * 用户余额比例，默认100
     */
    @Column(name = "consumer_balance_ratio")
    private Integer consumerBalanceRatio;

    /**
     * 赠送余额消费
     */
    @Column(name = "give_balance")
    private Integer giveBalance;

    /**
     * 余额消费
     */
    @Column(name = "balance")
    private Integer balance;


    /**
     * 洗车模式  0：自助洗车  1：全自动标准洗车 2：全自动增值洗车
     */
    @Column(name = "automatic_type")
    private Integer automaticType;

    /**
     * 大客户id
     */
    private Long agentId;

    /**
     * 订单业务扩展表id
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_business_id")
    private OrderBusiness orderBusiness;

    /**
     * 洗车订单是否评价
     */
    @Column(name = "is_wash_comment")
    private Boolean washComment;

    /**
     * 优惠券名称
     */
    private String couponName;


    /**
     * 清水单价
     **/
    private Integer waterPrice;
    /**
     * 清水消费金额
     **/
    private Integer waterBalance;

    /**
     * 泡沫单价
     **/
    private Integer frothPrice;
    /**
     * 泡沫消费金额
     **/
    private Integer frothBalance;

    /**
     * 洗手单价
     **/
    private Integer washPrice;
    /**
     * 洗手消费金额
     **/
    private Integer washBalance;


    /** 吸尘单价 */
    private Integer dushPrice;

    /** 吸尘消费金额 */
    private Integer dushBalance;


    /** 消毒时间**/
    private Integer disinfectTime;
    /** 消毒单价**/
    private Integer disinfectPrice;
    /** 消毒消费金额 **/
    private Integer disinfectBalance;

    /**
     * 加收比例
     */
    private Integer addedPrice;

    /**
     * 加收时段
     */
    private String addedPeriod;

    /**
     * 单次消费充值微信订单号
     */
    private String transactionId;

    /**
     * 单次消费支付时间
     */
    private Long payTime;

    /**
     * 用户已购消费卡id
     */
    private Long userConsumeCardId;

    /**
     * 订单总金额
     */
    private Integer orderPrice;

    /**
     * 订单类型，0-自助洗车，1-自助吸尘，2-全自动
     */
    private Integer consumpType;

    /**
     * 站点名称
     */
    private String siteName;

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

    public UtConsump() {
    }

    @Transient
    private Long createTimeStart;
    @Transient
    private Long createTimeEnd;

    public UtConsump(String icAccount, Integer realPrice, String equipId, Integer waterTime, Integer frothTime,
                     Integer dushTime, Integer washTime, Integer totalTime, Long createTime) {
        this.icAccount = icAccount;
        this.realPrice = realPrice;
        this.equipId = equipId;
        this.waterTime = waterTime;
        this.frothTime = frothTime;
        this.dushTime = dushTime;
        this.washTime = washTime;
        this.totalTime = totalTime;
        this.createTime = createTime;
    }

}
