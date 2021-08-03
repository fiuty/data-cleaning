package com.chebianjie.datacleaning.dto;

import lombok.Data;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2021-07-29
 */

public class ConsumerPhoneDTO {

    private Integer consumerId;

    private String phone;

    public Integer getConsumerId() {
        return consumerId;
    }

    public String getPhone() {
        return phone;
    }

    public ConsumerPhoneDTO(Integer consumerId, String phone) {
        this.consumerId = consumerId;
        this.phone = phone;
    }

    public ConsumerPhoneDTO(Integer consumerId) {
        this.consumerId = consumerId;
    }
}

