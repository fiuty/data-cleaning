package com.chebianjie.datacleaning.controller;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.UtCouponUser;
import com.chebianjie.datacleaning.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractBaseController {

    @Autowired
    protected CbjUtConsumerService cbjUtConsumerService;

    @Autowired
    protected ChjUtConsumerService chjUtConsumerService;

    @Autowired
    protected ConsumerLogService consumerLogService;

    @Autowired
    protected ConsumerCouponLogService consumerCouponLogService;

    @Autowired
    protected ConsumerBalanceService consumerBalanceService;

    @Autowired
    protected ConsumerService consumerService;

    @Autowired
    protected UtCouponUserService utCouponUserService;

    /**
     * 处理同一unionid多条数据情况
     * @param cbjUtConsumer
     * @return
     */
    protected UtConsumer fixCbjUtConsumer(UtConsumer cbjUtConsumer){
        if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())) {
            List<UtConsumer> utConsumerList = cbjUtConsumerService.listByUnionid(cbjUtConsumer.getUnionid());
            if(utConsumerList.size() > 1){
                //整合所有数据
                cbjUtConsumer =  utConsumerList.stream().min(Comparator.comparing(UtConsumer::getCreatetime)).get();
                cbjUtConsumer.setBalance(utConsumerList.stream().mapToInt(UtConsumer::getBalance).sum());
                cbjUtConsumer.setGiveBalance(utConsumerList.stream().mapToInt(UtConsumer::getGiveBalance).sum());
                cbjUtConsumer.setConsumptionAmount(utConsumerList.stream().filter(e -> e.getConsumptionAmount() != null).mapToInt(UtConsumer::getConsumptionAmount).sum());
                cbjUtConsumer.setOrderNum(utConsumerList.stream().filter(e -> e.getOrderNum() != null).mapToLong(UtConsumer::getOrderNum).sum());
            }
        }
        return cbjUtConsumer;
    }

    /**
     * 根据车便捷用户获取车惠捷用户
     * @param cbjUtConsumer
     * @return
     */
    protected UtConsumer fixChjUtConsumer(UtConsumer cbjUtConsumer){
        UtConsumer rst = null;
        List<UtConsumer> chjUtConsumerListByUnionId = new ArrayList<>();
        List<UtConsumer> chjUtConsumerListByAccount;
        List<UtConsumer> chjUtConsumerList;
        //ut_consumer表jhi_account非空
        chjUtConsumerListByAccount = chjUtConsumerService.getUtConsumerListByAccount(cbjUtConsumer.getAccount());
        if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
            chjUtConsumerListByUnionId = chjUtConsumerService.getUtConsumerListByUnionid(cbjUtConsumer.getUnionid());
        }
        //整合list
        chjUtConsumerListByAccount.addAll(chjUtConsumerListByUnionId);
        chjUtConsumerList = chjUtConsumerListByAccount;
        if(chjUtConsumerList.size() > 1){
            //整合所有数据
            rst =  chjUtConsumerList.stream().min(Comparator.comparing(UtConsumer::getCreatetime)).get();
            rst.setBalance(chjUtConsumerList.stream().mapToInt(UtConsumer::getBalance).sum());
            rst.setGiveBalance(chjUtConsumerList.stream().mapToInt(UtConsumer::getGiveBalance).sum());
            rst.setConsumptionAmount(chjUtConsumerList.stream().filter(e -> e.getConsumptionAmount() != null).mapToInt(UtConsumer::getConsumptionAmount).sum());
            rst.setOrderNum(chjUtConsumerList.stream().filter(e -> e.getOrderNum() != null).mapToLong(UtConsumer::getOrderNum).sum());
        }else if(chjUtConsumerList.size() == 1){
            rst = chjUtConsumerList.get(0);
        }
        return rst;
    }

    /**
     * 检测是否已清洗 - 用户
     * @param cbjUtConsumer
     * @return false - 未清洗  true - 已清洗
     */
    protected Boolean checkCleanConsumer(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        boolean rst = false;
        if(chjUtConsumer == null) {
            if (StrUtil.isNotBlank(cbjUtConsumer.getUnionid())) {
                if (consumerLogService.getOneByUnionId(cbjUtConsumer.getUnionid(), 1, 1) != null) {
                    rst = true;
                }
            } else if (StrUtil.isNotBlank(cbjUtConsumer.getAccount())) {
                if (consumerLogService.getOneByCbjAccount(cbjUtConsumer.getAccount(), 1, 1) != null) {
                    rst = true;
                }
            }
        }else{
            if(consumerLogService.getOneByChjIdAndStatusAndType(chjUtConsumer.getId(), 1, 1) != null){
                rst = true;
            }
        }
        return rst;
    }

    /**
     * 检测是否已清洗 - 优惠券
     * @param utCouponUser
     * @param  type  1.用户优惠券迁移 - 车便捷 2.用户优惠券迁移 - 车惠捷
     * @return false - 未清洗  true - 已清洗
     */
    protected Boolean checkCleanCoupon(UtCouponUser utCouponUser, int type){
        boolean rst = false;
        if(utCouponUser.getConsumerId() != null ) {
            if (consumerCouponLogService.getOneByUtCouponUserIdAndStatusAndType(utCouponUser.getId(), 1, type) != null) {
                rst = true;
            }
        }
        return rst;
    }
}
