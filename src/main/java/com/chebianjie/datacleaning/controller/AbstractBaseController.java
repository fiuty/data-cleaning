package com.chebianjie.datacleaning.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.service.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    protected ConsumerBalanceService consumerBalanceService;

    @Autowired
    protected ConsumerService consumerService;

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
        List<UtConsumer> chjUtConsumerList;
        if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
            chjUtConsumerList = chjUtConsumerService.getUtConsumerListByUnionid(cbjUtConsumer.getUnionid());
        }else{
            chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(cbjUtConsumer.getAccount());
        }

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
     * 检测是否已清洗
     * @param cbjUtConsumer
     * @return false - 未清洗  true - 已清洗
     */
    protected Boolean checkClean(UtConsumer cbjUtConsumer){
        boolean rst = false;
        if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())) {
            if (consumerLogService.getOneByUnionId(cbjUtConsumer.getUnionid(), 1, 1) != null) {
                rst = true;
            }
        }else if(StrUtil.isNotBlank(cbjUtConsumer.getAccount())){
            if(consumerLogService.getOneByCbjAccount(cbjUtConsumer.getAccount(), 1, 1) != null){
                rst = true;
            }
        }
        return rst;
    }
}
