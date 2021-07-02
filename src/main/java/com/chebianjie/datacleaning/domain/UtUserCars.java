package com.chebianjie.datacleaning.domain;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ut_user_cars")
@Data
public class UtUserCars {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "jhi_uid")
    private Long uid;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 品牌id
     */
    @Column(name = "brand_id")
    private Long brandId;

    /**
     *  车系
     */
    @Column(name = "car_system")
    private String carSystem;

    /**
     *  车系id
     */
    @Column(name = "car_system_id")
    private Long carSystemId;

    /**
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 型号id
     */
    @Column(name = "model_id")
    private Long modelId;

    /**
     * 购买时间
     */
    @Column(name = "purchase_time")
    private Long purchaseTime;

    /**
     * 行驶公里
     */
    @Column(name = "distance")
    private Long distance;

    /**
     * 车牌号
     */
    @Column(name = "car_num")
    private String carNum;

    /**
     * 车架号
     */
    @Column(name = "car_frame_num")
    private String carFrameNum;

    /**
     * 发动机号
     */
    @Column(name = "engine_num")
    private String engineNum;

    /**
     * 保险公司
     */
    @Column(name = "insurance_compan")
    private String insuranceCompan;

    /**
     * 保险到期时间
     */
    @Column(name = "expire_time")
    private Long expireTime;

    /**
     * 车主姓名
     */
    @Column(name = "car_owner")
    private String carOwner;

    /**
     * 认证
     */
    @Column(name = "jhi_validate")
    private Long jhiValidate;

    /**
     * 默认车辆
     */
    @Column(name = "is_default")
    private Long isDefault;

    /**
     * 车辆图标
     */
    @Column(name = "logo")
    private String logo;

    /**
     * 行驶证正面照片地址
     */
    @Column(name = "photo_url_front")
    private String photoUrlFront;

    /**
     * 行驶证反面照片地址
     */
    @Column(name = "photo_url_back")
    private String photoUrlBack;

    /**
     * 车辆等级
     */
    @Column(name = "jhi_level")
    private Long jhiLevel;

    /**
     * 地区名字（省份）
     */
    @Column(name = "area")
    private String area;

    /**
     * 车险品牌id
     */
    @Column(name = "insurance_type")
    private String insuranceType;

    /**
     * 投保城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 更新时间
     */
    @Column(name = "updatetime")
    private Long updatetime;

    /**
     * 用户昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 用户真实姓名
     */
    @Column(name = "realname")
    private String realname;

    /**
     * 身份证
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 车辆类型
     */
    @Column(name = "car_type")
    private Integer carType;

    /**
     * 上次年检至今是否造成人伤事故 1:是  0:否
     */
    @Column(name = "accident_type")
    private Integer accidentType;

    /**
     * 来源。1-公众号，2-app，3-小程序
     */
    @Column(name = "date_from")
    private Integer dateFrom;

    /**
     * 汽车品牌详情id
     */
    private Long carDetailId;





}
