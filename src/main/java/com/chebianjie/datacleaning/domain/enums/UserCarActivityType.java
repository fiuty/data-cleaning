package com.chebianjie.datacleaning.domain.enums;

/**
 * 活动状态：READY-未参加、EXAMINE-待审核、PASS_UN_SEND-已通过（未发送奖励）、PASS_SEND-已通过（发送奖励）
 * @author zhengdayue
 * @date: 2021-07-23
 */
public enum UserCarActivityType {

    //未参加
    READY,

    //待审核
    EXAMINE,

    //已通过（未发送奖励）
    PASS_UN_SEND,

    //已通过（发送奖励）
    PASS_SEND,
}
