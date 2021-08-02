package com.chebianjie.datacleaning.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Data
@Table(name = "ut_user_driver_licens")
public class UtUserDriverLicens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "jhi_uid")
    private Long uid;

    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String name;

    /**
     * 性别
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 兴趣
     */
    @Column(name = "interest")
    private String interest;

    /**
     * 国籍
     */
    @Column(name = "nationality")
    private String nationality;

    /**
     * 住址
     */
    @Column(name = "address")
    private String address;

    /**
     * 出生日期
     */
    @Column(name = "date_of_birth")
    private String dateOfBirth;

    /**
     * 初次发证日期
     */
    @Column(name = "date_of_first_lusse")
    private String dateOfFirstLusse;

    /**
     * 准驾车型
     */
    @Column(name = "car_class")
    private String carClass;

    /**
     * 有效起始日期
     */
    @Column(name = "valid_period")
    private String validPeriod;

    /**
     * 驾照正面图案
     */
    @Column(name = "photo_url_front")
    private String photoUrlFront;

    /**
     * 驾照背面图案
     */
    @Column(name = "photo_url_back")
    private String photoUrlBack;

    /**
     * 驾驶证编码
     */
    @Column(name = "license_code")
    private String licenseCode;

    @Column(name = "consumer_union_account")
    @ApiModelProperty("用户唯一标识")
    private String consumerUnionAccount;


}
