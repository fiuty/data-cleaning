package com.chebianjie.datacleaning.domain.enums;

/**
 * @author 57211
 * @create 2021-06-03 9:54
 */
public enum ConsumerStatus {
    //正常
    NORMAL,
    //禁用
    FORBIDEN;

    /**
     * 根据status返回枚举
     * @param status
     * @return
     */
    public static ConsumerStatus fixConsumerStatus(int status){
        ConsumerStatus consumerStatus = null;
        switch (status){
            case 1:
                consumerStatus = ConsumerStatus.NORMAL;
                break;
            case 2:
                consumerStatus = ConsumerStatus.FORBIDEN;
                break;
        }
        return consumerStatus;
    }
}
