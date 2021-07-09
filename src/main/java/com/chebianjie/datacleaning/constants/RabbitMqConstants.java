package com.chebianjie.datacleaning.constants;

import lombok.Data;

/**
 * @author zhengdayue
 */
@Data
public class RabbitMqConstants {

    //consumerBill后续监听
    public static final String DATA_CLEAN_BILL_QUEUE = "data.clean.bill.queue";

    public static final String DATA_CLEAN_BILL_EXCHANGE = "data.clean.bill.exchange";

    public static final String DATA_CLEAN_BILL_ROUTING_KEY = "data.clean.bill.routing.key";

    //流水第一阶段清洗
    public static final String DATA_CLEAN_FIRST_BILL_QUEUE = "data.clean.first.bill.queue";

    public static final String DATA_CLEAN_FIRST_BILL_EXCHANGE = "data.clean.first.bill.exchange";

    public static final String DATA_CLEAN_FIRST_BILL_ROUTING_KEY = "data.clean.first.bill.routing.key";

    //员工业绩清洗
    public static final String DATA_CLEAN_FIRST_STAFF_LOG_QUEUE = "data.clean.first.staff.log.queue";

    public static final String DATA_CLEAN_FIRST_STAFF_LOG_EXCHANGE = "data.clean.first.staff.log.exchange";

    public static final String DATA_CLEAN_FIRST_STAFF_LOG_ROUTING_KEY = "data.clean.first.staff.log.routing.key";


}
