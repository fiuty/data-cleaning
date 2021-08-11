package com.chebianjie.datacleaning.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
@Data
@Table
@Entity
public class FailConsumerBillLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String consumerAccount;

}
