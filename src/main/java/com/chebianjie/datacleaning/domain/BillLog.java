package com.chebianjie.datacleaning.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author zhengdayue
 * @date: 2021-07-01
 */
@Entity
@Table(name = "bill_log")
@Data
public class BillLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 消费者唯一账号
     */
    private String unionAccount;

    /**
     * 状态 0: 失败 1:成功
     */
    private int status;
}
