package com.chebianjie.datacleaning.domain.enums;

/**
 * @author 57211
 * @create 2021-06-03 9:56
 */
public enum Gender {

    /**
     * 男
     */
    MAN,
    /**
     * 女
     */
    WOMAN,
    /**
     * 未知
     */
    UNKNOWN;

    /**
     * 根据sex返回枚举
     * @param sex
     * @return
     */
    public static Gender fixGender(int sex){
        Gender gender = null;
        switch (sex){
            case 1:
                gender = Gender.MAN;
                break;
            case 2:
                gender = Gender.WOMAN;
                break;
            default:
                gender = Gender.UNKNOWN;
        }
        return gender;
    }
}
