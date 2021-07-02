package com.chebianjie.datacleaning.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@Table(name = "ut_coupon")
public class UtCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 类型(1:代金券 2:满减券 3:折扣券 4:洗车券金额)
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 1 代金券金额  2  满减券  满多少  3 折扣券 折扣  4:洗车券金额
     */
    @Column(name = "price")
    private Integer price;

    /**
     * 2  满减券  减多少
     */
    @Column(name = "reduction")
    private Integer reduction;

    /**
     * 适用范围(0:全平台 1:指定站点)
     */
    @Column(name = "scope")
    private Integer scope;

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
     * 到期后自动续期一个月(0:不续期 1:续期)
     */
    @Column(name = "renewal")
    private Integer renewal;

    /**
     * 发行量(-1:无限 大于0:数量)
     */
    @Column(name = "circulation_nums")
    private Long circulationNums;

    /**
     * 可送量(-1:无限 可送量 = 优惠券的总发行量 - 员工持有 - 用户已领取)
     */
    @Column(name = "available_nums")
    private Long availableNums;

    /**
     * 已领取量
     */
    @Column(name = "received_nums")
    private Long receivedNums;

    /**
     * 已使用量
     */
    @Column(name = "used_nums")
    private Long usedNums;

    /**
     * 促成交易金额
     */
    @Column(name = "transaction_amount")
    private Long transactionAmount;

    /**
     * 客单价
     */
    @Column(name = "unit_amount")
    private Long unitAmount;

    /**
     * 领取门槛(0:全用户  1:新注册用户)
     */
    @Column(name = "threshold")
    private Integer threshold;

    /**
     * 每人限领次数(-1:无限领取  大于0:数量 )
     */
    @Column(name = "limit_times")
    private Long limitTimes;

    /**
     * 有效天数
     */
    @Column(name = "valid_date")
    private Long validDate;

    /**
     * 状态 0 未开始  1 正进行  2 已结束
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 0 禁止发券  1 正常发券
     */
    @Column(name = "enabled")
    private Integer enabled;

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
     * 一天内限领次数(-1 无限制)
     */
    private Integer dayObtainLimit;

    /**
     * 一天内限用次数(-1 无限制)
     */
    private Integer dayUseLimit;

    /**
     * 适用业务：0-全部、1-洗车、2-车服
     */
    private Integer business;

}
