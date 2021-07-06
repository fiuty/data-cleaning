package com.chebianjie.datacleaning.domain.order;

import lombok.Data;

import javax.persistence.*;

/**
 * 订单业务扩展表
 * @author zhengdayue
 */
@Data
@Entity
@Table
public class OrderBusiness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 单次消费充值微信订单号
     */
    private String transactionId;

    /**
     * 用户已购消费卡id
     */
    private Long userConsumeCardId;

//    /**
//     * 订单加收价格记录表id
//     */
//    private Long consumpAddPriceLogId;


}
