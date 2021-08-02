package com.chebianjie.datacleaning.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


@Entity
@Data
@Table(name = "account_integral")
public class AccountIntegral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "jhi_uid")
    private Long uid;

    /**
     * 积分
     */
    @Column(name = "integral")
    private Long integral;

    /**
     * 已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private ZonedDateTime createTime;

    /**
     * 删除时间
     */
    @Column(name = "delete_time")
    private ZonedDateTime deleteTime;

    /**
     * 上次更新时间
     */
    @Column(name = "last_update_time")
    private ZonedDateTime lastUpdateTime;

    /**
     * 操作添加用户
     */
    @Column(name = "create_uid")
    private Long createUid;

    /**
     * 操作删除用户
     */
    @Column(name = "delete_uid")
    private Long deleteUid;

    /**
     * 上次更新用户
     */
    @Column(name = "last_update_uid")
    private Long lastUpdateUid;


    /**
     * 用户唯一标识
     */
    @Column(name = "consumer_union_account")
    private String consumerUnionAccount;




}
