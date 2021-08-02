package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2020-12-30
 */
@Data
@Table
@Entity
@DynamicUpdate
@DynamicInsert
public class StaffInviteDetail {


    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 员工id */
    private Integer staffId;
    /** 用户手机号 */
    private String phone;
    /** 用户id */
    private Long consumerId;
    /** 用户头像 */
    private String avatar;
    /** 邀请时间 */
    private Long inviteTime;

    /**
     *  用户唯一标识
     */
    private String consumerUnionAccount;





}

