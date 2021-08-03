package com.chebianjie.datacleaning.dto;

import lombok.Data;

/**
 * @author: wpan
 * @Description:
 * @Version :0.0.1
 * @Date: 2021-07-29
 */

public class ConsumerPhoneDTO {

    private Long consumerId;

    private String phone;

    public Long getConsumerId() {
        return consumerId;
    }

    public String getPhone() {
        return phone;
    }

    public ConsumerPhoneDTO(Long consumerId, String phone) {
        this.consumerId = consumerId;
        this.phone = phone;
    }

    public ConsumerPhoneDTO(Long consumerId) {
        this.consumerId = consumerId;
    }
}

