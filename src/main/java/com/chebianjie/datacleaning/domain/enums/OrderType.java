package com.chebianjie.datacleaning.domain.enums;

/**
 * 订单类型：洗车业务：SELF_WASH-自助洗车、AUTO_WASH-自动洗车、DUST-吸尘、SERVER-增值服务；支付业务：CHARGE_USER-用户自充、
 * CHARGE_AGENT-大客户充值、CHARGE_STAFF-员工代充、CHARGE_MANAGER-后台充值、CHARGE_GIVE-注册赠送、CHARGE_IC_CARD-员工代充ic卡；
 * 退款业务：REFUND；余额转移：BALANCE_TRANSFER-余额已转移、BALANCE_COME-余额已转入
 *
 * @author 57211
 * @create 2021-06-03 10:06
 */
public enum OrderType {

    /**
     * 洗车业务
     */
    //自助洗车
    SELF_WASH,
    //自动洗车
    AUTO_WASH,
    //吸尘
    DUST,
    //增值服务
    SERVER,

    /**
     * 支付业务
     */
    //用户自充
    CHARGE_USER,
    //大客户充值
    CHARGE_AGENT,
    //员工代充
    CHARGE_STAFF,
    //后台充值
    CHARGE_MANAGER,
    //注册赠送
    CHARGE_GIVE,
    //员工代充ic卡
    CHARGE_IC_CARD,

    /**
     * 退款业务
     */
    //余额清零
    RESET,
    //撤销退款
    CANCEL_REFUND,
    //余额已转移
    BALANCE_TRANSFER,
    //余额已转入
    BALANCE_COME,
    //全自动退款
    AUTO_REFUND,
    //商城退款,
    MALL_REFUND,
    //门店消费退款
    STORE_REFUND

}
