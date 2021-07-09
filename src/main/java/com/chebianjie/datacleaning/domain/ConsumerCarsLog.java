package com.chebianjie.datacleaning.domain;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;
import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * @Author: matingting
 * @Date: 2021/06/30/17:54
 */
@Data
@Table(name = "consumer_cars_log")
@Entity
public class ConsumerCarsLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * ut_user_cars的id
     */
    private long userCarsId;


    /**
     * ut_user_cars的uid(用户id)
     */
    private long uid;


    /**
     * ut_consumer的jhi_account(手机号)
     */
    private String account;


    /**
     * 来源平台
     */
    @Enumerated(EnumType.STRING)
    private Platform platform;


    /**
     * 0-失败 1-成功
     */
    private int status;


    /**
     * 车详情id
     */
    private Long carDetailId;


    /**
     * userplatform-consumer-消费者唯一账号
     */
    private String unionAccount;



    private String unionId;


    private LocalDateTime creatTime;





}
