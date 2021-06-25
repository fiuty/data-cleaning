package com.chebianjie.datacleaning.domain.enums;

/**
 * 订单支付类型：ORDINARY-普通洗车、AGENT-大客户洗车、ONCE-单次消费洗车、SINOPEC-中石化优惠券洗车、CONSUMERCARD-消费卡洗车、SERVER-增值服务代洗
 * @author zhengdayue
 * @date: 2021-06-03
 */
public enum OrderPaymentType {
    //普通洗车
    ORDINARY,
    //大客户洗车
    AGENT,
    //单次消费洗车
    ONCE,
    //中石化优惠券洗车
    SINOPEC,
    //消费卡洗车
    CONSUMERCARD,
    //增值服务代洗
    SERVER,
}
