package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerCouponLog;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtCouponUser;
import com.chebianjie.datacleaning.service.ConsumerCouponLogService;
import com.chebianjie.datacleaning.service.ConsumerLogService;
import com.chebianjie.datacleaning.service.UtCouponUserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ConsumerCouponTask implements Runnable {

    private UtCouponUserService utCouponUserService;

    private ConsumerCouponLogService consumerCouponLogService;

    private UtCouponUser utCouponUser;

    private Consumer consumer;

    /**
     * 1:车便捷 2:车惠捷
     */
    private int type;

    public ConsumerCouponTask(UtCouponUserService utCouponUserService, ConsumerCouponLogService consumerCouponLogService, UtCouponUser utCouponUser, Consumer consumer, int type){
        this.utCouponUserService = utCouponUserService;
        this.consumerCouponLogService = consumerCouponLogService;
        this.utCouponUser = utCouponUser;
        this.consumer = consumer;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            if (type == 1) {
                //补全union_account - 车便捷
                utCouponUserService.mergeCbjConsumerCoupon(utCouponUser, consumer);
                //3.迁移成功记录日志
                //判断是否有失败记录, 有则更新 无则插入
                ConsumerCouponLog curConsumerCouponLog = consumerCouponLogService.getOneByUtCouponUserIdAndStatusAndType(utCouponUser.getConsumerId(), 0, type);
                if(curConsumerCouponLog != null) {
                    curConsumerCouponLog.setStatus(1);
                    consumerCouponLogService.saveOne(curConsumerCouponLog);
                }else{
                    ConsumerCouponLog temp = new ConsumerCouponLog();
                    temp.setUtCouponUserId(utCouponUser.getId());
                    temp.setType(type);
                    temp.setStatus(1);
                    consumerCouponLogService.saveOne(temp);
                }
            } else if (type == 2) {
                //补全union_account - 车惠捷
                utCouponUserService.mergeChjConsumerCoupon(utCouponUser, consumer);
                //3.迁移成功记录日志
                //判断是否有失败记录, 有则更新 无则插入
                ConsumerCouponLog curConsumerCouponLog = consumerCouponLogService.getOneByUtCouponUserIdAndStatusAndType(utCouponUser.getConsumerId(), 0, type);
                if(curConsumerCouponLog != null) {
                    curConsumerCouponLog.setStatus(1);
                    consumerCouponLogService.saveOne(curConsumerCouponLog);
                }else{
                    ConsumerCouponLog temp = new ConsumerCouponLog();
                    temp.setUtCouponUserId(utCouponUser.getId());
                    temp.setType(type);
                    temp.setStatus(1);
                    consumerCouponLogService.saveOne(temp);
                }
            }
        }catch(Exception e){
            log.error(e.getMessage());
            //4. 有任何报错记录log 失败
            ConsumerCouponLog temp = new ConsumerCouponLog();
            temp.setUtCouponUserId(utCouponUser.getId());
            temp.setType(type);
            temp.setStatus(0);
            consumerCouponLogService.saveOne(temp);
        }
    }
}
