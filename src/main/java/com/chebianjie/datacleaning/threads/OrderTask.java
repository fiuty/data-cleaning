package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.service.OrderService;
import lombok.Data;

/**
 * @author: wpan
 * @Description: 迁移洗车订单任务
 * @Version :0.0.1
 * @Date: 2021-07-01
 */
@Data
public class OrderTask implements Runnable {


    private OrderService orderService;

    private Integer consumerId;

    private String phone;

    private String consumerAccount;

    /**
     * type  1:车便捷洗车订单  2:车惠捷洗车订单  3:车便捷充值订单 4:车惠捷充值订单
     */
    private Integer type;


    public OrderTask(OrderService orderService, Integer consumerId, String phone, String consumerAccount, Integer type) {
        this.orderService = orderService;
        this.consumerId = consumerId;
        this.phone = phone;
        this.consumerAccount = consumerAccount;
        this.type = type;
    }


    @Override
    public void run() {
        if (type == 1) {
            orderService.cleaningCBJWashOrder(consumerId, phone, consumerAccount);
        }
        if (type == 2) {
            orderService.cleaningCHJWashOrder(consumerId, phone, consumerAccount);
        }
    }
}

