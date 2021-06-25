package com.chebianjie.datacleaning.domain.enums;

/**
 * 订单状态:通用状态：FINISH-完成、CLOSE-关闭；洗车业务：UNSTART-未启动机器；充值状态：CHARGE_SUCCESS-支付成功、
 * CHARGE_READY-待支付、CHARGE_FAIL-支付失败、CHARGE_CANCEL-用户取消支付、CHARGE_FINISH-已完成；
 * 增值服务：SERVER_NO_CHECK-待完成-待验证、SERVER_CHECK-待完成-已验证、SERVER_TIME_OUT-超时未验证、SERVER_FINISH-已完成、
 * SERVER_CANCEL_REFUNDING-已取消-退款中、CHARGE_FINISH-已取消-已退款；退款-余额清零：REFUND_APPLY-申请中、REFUND_AGREE-已同意、REFUND_REJECT-已拒绝
 *
 * @author 许泽坤
 * @create 2021-06-03 10:16
 */
public enum OrderStatus {
    /**
     * 通用状态
     */
    //完成
    FINISH,
    //关闭
    CLOSE,


    /**
     * 洗车业务
     */
    UNSTART,

    /**
     * 商城业务
     */

    /**
     * 电单车业务
     */

    /**
     * 充值状态
     */
    //支付成功
    CHARGE_SUCCESS,
    //待支付
    CHARGE_READY,
    //支付失败
    CHARGE_FAIL,
    //用户取消支付
    CHARGE_CANCEL,
    //已完成
    CHARGE_FINISH,

    /**
     * 增值服务
     */
    //待完成-待验证
    SERVER_NO_CHECK,
    //待完成-已验证
    SERVER_CHECK,
    //超时未验证
    SERVER_TIME_OUT,
    //已完成
    SERVER_FINISH,
    //已取消-退款中
    SERVER_CANCEL_REFUNDING,
    //已取消-已退款
    SERVER_CANCEL_REFUND,

    /**
     * 退款-余额清零
     */
    //申请中
    REFUND_APPLY,
    //已同意
    REFUND_AGREE,
    //已拒绝
    REFUND_REJECT,
}
