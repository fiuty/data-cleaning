package com.chebianjie.datacleaning.domain.enums;

/**
 * 消费者余额类型:真实充值余额-REAL_BALANCE、赠送余额-GIVE_BALANCE、大客户余额-AGENT_BALANCE、大客户赠送余额-AGENT_GIVE_BALANCE
 * @author 57211
 * @create 2021-06-03 9:58
 */
public enum BalanceType {

    /**
     * 真实充值余额
     */
    REAL_BALANCE,
    /**
     * 赠送余额
     */
    GIVE_BALANCE,
    /**
     * 大客户余额
     */
    AGENT_BALANCE,
    /**
     * 大客户赠送余额
     */
    AGENT_GIVE_BALANCE,
}
