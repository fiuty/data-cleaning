package com.chebianjie.datacleaning.domain.enums;

/**
 * 用户使用客户端类型
 *
 * @author 许泽坤
 * @create 2021-06-24 10:47
 */
public enum ClientType {

    /**
     * APP
     */
    APP,
    /**
     * 微信公众号
     */
    WECHAT_MP,
    /**
     * 小程序
     */
    WECHAT_MINI_APP,
    /**
     * 网页版
     */
    H5;

    /**
     *  根据dataFrom返回枚举
     * @param dataFrom
     * @return
     */
    public static ClientType fixClientType(int dataFrom){
        ClientType clientType = null;
        switch (dataFrom){
            case 1:
                clientType = ClientType.WECHAT_MP;
                break;
            case 2:
                clientType = ClientType.APP;
                break;
            case 3:
                clientType = ClientType.WECHAT_MINI_APP;
                break;
        }
        return clientType;
    }
}
