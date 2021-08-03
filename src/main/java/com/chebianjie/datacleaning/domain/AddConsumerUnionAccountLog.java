package com.chebianjie.datacleaning.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class AddConsumerUnionAccountLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * type的table主键
     */
    private Long tId;

    /**
     * 车便捷consumerId
     */
    private Long cbjId;

    /**
     * 车惠捷consumerId
     */
    private Long chjId;

    /**
     * 用户唯一标识
     */
    private String consumerUnionAccount;

    /**
     * 状态 0-失败 1-成功
     */
    private int status;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 分类
     */
    private int type;



}
