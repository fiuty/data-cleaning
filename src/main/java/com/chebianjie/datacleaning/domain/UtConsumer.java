package com.chebianjie.datacleaning.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户信息表
 * @author xiaoze
 * @Date 2018/11/16
 */
@Data
@Entity
@Table(name = "ut_consumer")
public class UtConsumer implements Serializable {

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
     * 洗车端infomem的id（1.0版本的userinfo ID）
     */
    @Column(name = "sid")
    private Integer sid;

    /**
     * nickname(默认为用户微信名称)
     */
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "unionid", nullable = false)
    private String unionid;

    @Column(name = "miniapp_openid", nullable = false)
    private String miniappOpenid;

    /**
     * avatar(默认为用户微信头像，用户可改)
     */
    @Column(name = "avatar", nullable = false)
    private String avatar;

    /**
     * oss头像key
     */
    @Column(name = "oss_key")
    private String ossKey;

    /**
     * 微信openid
     */
    @Column(name = "openid", nullable = false)
    private String openid;

    /**
     * 账户名(即用户手机号)
     */
    @Column(name = "jhi_account", nullable = false, unique = true)
    private String account;

    /**
     * 密码
     */
    @Column(name = "pwd")
    private String pwd;

    /**
     * 洗车密码，因为洗车机只有数字键盘（默认取手机号后6位）
     */
    @Column(name = "pwd_2")
    private String pwd2;

    /**
     * 用户手机号码
     */
    @Column(name = "phone", nullable = false)
    private String phone;

    /**
     * 创建时间（注册时间）
     */
    @Column(name = "createtime", nullable = false)
    private Long createtime;

    @Transient
    private Long createTimeStart;
    @Transient
    private Long createTimeEnd;

    /**
     * 最后一次访问时间
     */
    @Column(name = "lastlogintime", nullable = false)
    private Long lastlogintime;

    /**
     * 最后一次洗车时间
     */
    @Column(name = "usetime")
    private Long usetime;

    /**
     * 余额（以分为单位，以后主要使用这个）
     */
    @Column(name = "balance", nullable = false)
    private int balance;

    /**
     * 赠送余额（以分为单位，以后主要使用这个）
     */
    @Column(name = "give_balance")
    private int giveBalance;

    /**
     * 积分
     */
    @Column(name = "integral")
    private Integer integral;

    /**
     * 余额（以元为单位，这个是过渡的，后期准备淘汰，全部使用以分为单位）
     */
    @Column(name = "credit_2", precision = 10, scale = 2)
    private BigDecimal credit2;

    /**
     * 首推荐人的ID（ut_staff表中的staff_id。业务员id，首推业务员终身享受充值提成）
     */
    @Column(name = "referrer")
    private Integer referrer;

    /**
     * 真实姓名
     */
    @Column(name = "realname")
    private String realname;

    /**
     * 状态1正常2封号3其它异常状态
     */
    @Column(name = "statue", nullable = false)
    private Integer statue;

    /**
     * 用户来源。1-公众号，2-app，3-小程序
     */
    @Column(name = "data_from")
    private Integer dataFrom;

    @Column(name = "stair_province_id")
    private Long stairProvinceId;

    @Column(name = "stair_city_id")
    private Long stairCityId;

    @Column(name = "stair_area_id")
    private Long stairAreaId;

    @Column(name = "stair_site_id")
    private Long stairSiteId;

    @Column(name = "stair_site_binding_time")
    private Long stairSiteBindingTime;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "age")
    private Integer age;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "user_level")
    private Integer userLevel;

    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "province")
    private String province;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "city")
    private String city;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "area")
    private String area;

    @Column(name = "order_num")
    private Long orderNum;

    @Column(name = "consumption_amount")
    private Integer consumptionAmount;

    @Column(name = "stair_site_code")
    private String stairSiteCode;

    @Column(name = "stair_site_name")
    private String stairSiteName;

    @Transient
    private Long[] ids;

}
