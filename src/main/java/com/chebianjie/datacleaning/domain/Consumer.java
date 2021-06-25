package com.chebianjie.datacleaning.domain;


import com.chebianjie.datacleaning.domain.enums.ClientType;
import com.chebianjie.datacleaning.domain.enums.ConsumerStatus;
import com.chebianjie.datacleaning.domain.enums.Gender;
import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created on 2021/5/28.
 * 消费者表
 *
 * @author 许泽坤
 * @version 1.0.0
 * @date 2021/5/28
 */
@Data
@Table
@Entity
@NoArgsConstructor
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 消费者唯一账号
     */
    private String unionAccount;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册时间
     */
    private LocalDateTime registryTime;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 注册平台
     */
    @Enumerated(EnumType.STRING)
    private Platform registryPlatform;

    /**
     * 注册客户端类型
     */
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 登陆密码
     */
    private String loginPassword;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 推荐人
     */
    private String referrer;

    /**
     * 消费者状态
     */
    @Enumerated(EnumType.STRING)
    private ConsumerStatus status;

    /**
     * 微信公众号OpenId
     */
    private String wechatMpOpenId;

    /**
     * 微信小程序OpenId
     */
    private String wechatMiniAppOpenId;

    /**
     * 微信UnionId
     */
    private String wechatUnionId;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 绑定站点ID
     */
    private Long bindSiteId;

    /**
     * 绑定站点名称
     */
    private String bindSiteName;

    /**
     * 绑定站点时间
     */
    private LocalDateTime bindSiteTime;

    /**
     * 绑定站点平台
     */
    @Enumerated(EnumType.STRING)
    private Platform bindSitePlatform;

    /**
     * 下单总次数
     */
    private Integer consumptionNum;

    /**
     * 消费总金额
     */
    private Integer consumptionPrice;

    /**
     * 积分
     */
    private Integer integral;

}


