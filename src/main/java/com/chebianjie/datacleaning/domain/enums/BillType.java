package com.chebianjie.datacleaning.domain.enums;

/**
 * 账单类型:充值-CHARGE、洗车-WASH、商城消费-MALL、门店消费-STORE、余额清零-RESET、余额转移-TRANSFER、购买消费卡-CONSUMER_CARD
 * 撤销退款-CANCEL_REFUND、增值服务-SERVER
 *
 * @author 57211
 * @create 2021-06-03 10:07
 */
public enum BillType {

    //充值
    CHARGE,
    //洗车
    WASH,
    //商城消费
    MALL,
    //门店消费
    STORE,
    //余额清零
    RESET,
    //余额转移
    TRANSFER,
    //购买消费卡
    CONSUMER_CARD,
    //撤销退款
    CANCEL_REFUND,
    //增值服务
    SERVER
}
