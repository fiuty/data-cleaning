package com.chebianjie.datacleaning.domain;

import com.chebianjie.common.core.enums.CarModel;
import com.chebianjie.common.core.enums.WashMachineClassModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author zhengdayue
 */
@Table
@Entity
@Data
public class ServerOrder{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 平台订单编号 */
    private String orderNum;

    /** 消费者手机号 */
    private String tel;

    /** 消费者id */
    private Long consumerId;

    /**
     * 车服服务id
     */
    private Long serverId;

    /** 服务项目名称 */
    private String serverName;

    /** 适用车型 */
    @Enumerated(EnumType.STRING)
    private CarModel carModel;

    /** 支付金额 */
    private Integer realPrice;

    /** 订单总金额 */
    private Integer orderPrice;

    /** 订单进行状态，-1未支付、0待完成-待验证、1待完成-已验证、3已完成 、4已取消 */
    private Integer status;

    /** 派单状态：1-可派单、2-已派单 */
    private Integer releaseStatus;

    /** 开始时间 */
    private Long startTime;

    /** 结束时间 */
    private Long endTime;

    /** 支付时间 */
    private Long payTime;

    /** 创建时间 */
    private Long createTime;

    /** 优惠券类型(1:代金券 2:满减券 3:折扣券  4:洗车券 5:中石化电子券) */
    private Integer couponType;

    /** 优惠券id */
    private Long couponUserId;

    /** 优惠券名称 */
    private String couponName;

    /** 优惠券金额 */
    private Integer couponPrice;

    /** 赠送余额消费金额 */
    private Integer giveBalance;

    /** 余额消费金额 */
    private Integer balance;

    /** 微信流水号 */
    private String transactionId;

    /**
     * 站点id
     */
    @Column(name = "site_id", insertable = false, updatable = false)
    private Long siteId;

    /** 站点名称 */
    private String siteName;

    /** 是否评价 0-待评价，1-已评价 */
    @Column(name = "is_wash_comment")
    private Boolean washComment;

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

    /** 支付类型：1-余额支付,2-微信支付 */
    private Integer payType;

    /**
     * 服务时间
     */
    private String serverTime;

    /**
     * 服务者
     */
    private String providerName;

    /**
     * 服务者联系方式
     */
    private String providerTel;

    /**
     * 平台id
     */
    private Integer platformId;

    /**
     * 是否需要启动洗车机完成服务,true-需要,false-不需要
     */
    @Column(name = "is_start_machine")
    private Boolean startMachine;

    /**
     * 服务者类型：1-员工，2-商家
     */
    private Integer providerType;

    /**
     * 服务者账号
     */
    private String providerAccount;


    /**
     * 用户头像
     */
    private String consumerPhoto;

    /**
     * 用户昵称
     */
    private String consumerName;

    /**
     * 订单当前记录状态:serverRecord的status
     */
    private Integer serverRecordStatus;

    /**
     * 是否允许员工接单
     */
    @Column(name = "is_staff_receive")
    private Boolean staffReceive;

    /**
     * 旧站点id
     */
    private Long oldSiteId;

    /**
     * 洗车订单类型（自助洗车机-SELF_WASH 自助吸尘器-SELF_DUSH 全自动洗车-AUTO_WASH）
     */
    @Enumerated(EnumType.STRING)
    private WashMachineClassModel washMachineClassModel;

    /**
     * 洗车订单号
     */
    private String washMachineOrderNum;

    /**
     * 用户所属平台
     */
    private Integer userPlatformId;

    /**
     * 用户平台openid
     */
    private String consumerOpenId;

    /**
     * 增值收益（需要启动机器收益算员工的，无需启动机器收益算商家的）
     */
    private Integer profit;

    /**
     * 站长电话号码
     */
    private String siteOwnerPhone;

    /**
     * 自动退款需要发送推送
     */
    @Column(name = "is_need_push")
    private Boolean needPush;

    /**
     * 预约到店服务时间日期格式
     */
    private LocalDate serverDate;

    /**
     * 商家平均收益,如果站点多个商家n,那么商家平均收益=profit/n
     */
    private Integer agentShareProfit;

    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;



}
