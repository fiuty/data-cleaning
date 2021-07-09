package com.chebianjie.datacleaning.controller;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.domain.enums.BalanceType;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.service.*;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
     * @param utConsumer
     * @param type 1:车便捷 2:车惠捷
     * @return
     */
        protected UtConsumer fixUtConsumerByAccount(UtConsumer utConsumer, int type){
        if(StrUtil.isNotBlank(utConsumer.getUnionid())) {
            List<UtConsumer> utConsumerList = new ArrayList<>();
            if(type == 1){
                utConsumerList = cbjUtConsumerService.getUtConsumerListByAccount(utConsumer.getUnionid());
            }else if(type == 2){
                utConsumerList = chjUtConsumerService.getUtConsumerListByAccount(utConsumer.getUnionid());
            }
            if(utConsumerList.size() > 1){
                //整合所有数据
                utConsumer =  utConsumerList.stream().max(Comparator.comparing(UtConsumer::getLastlogintime)).get();
                utConsumer.setBalance(utConsumerList.stream().mapToInt(UtConsumer::getBalance).sum());
                utConsumer.setGiveBalance(utConsumerList.stream().mapToInt(UtConsumer::getGiveBalance).sum());
                utConsumer.setConsumptionAmount(utConsumerList.stream().filter(e -> e.getConsumptionAmount() != null).mapToInt(UtConsumer::getConsumptionAmount).sum());
                utConsumer.setOrderNum(utConsumerList.stream().filter(e -> e.getOrderNum() != null).mapToLong(UtConsumer::getOrderNum).sum());
            }
        }
        return utConsumer;
    }

    /**
     * 根据车便捷用户获取车惠捷用户
     * @param cbjUtConsumer
     * @return UtConsumer
     */
    protected UtConsumer getChjUtConsumerByCbjUtConsumer(UtConsumer cbjUtConsumer){
        UtConsumer rst = null;
        List<UtConsumer> chjUtConsumerList;
        //ut_consumer表jhi_account非空
        chjUtConsumerList = chjUtConsumerService.getUtConsumerListByAccount(cbjUtConsumer.getAccount());
        //整合list
        if(chjUtConsumerList.size() > 1){
            //整合所有数据
            rst = chjUtConsumerList.stream().max(Comparator.comparing(UtConsumer::getLastlogintime)).get();
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
     * 根据车惠捷用户获取车便捷用户
     * @param chjUtConsumer
     * @return
     */
    protected UtConsumer getCbjUtConsumerByChjUtConsumer(UtConsumer chjUtConsumer){
        UtConsumer rst = null;
        List<UtConsumer> cbjUtConsumerList;
        //ut_consumer表jhi_account非空
        cbjUtConsumerList = cbjUtConsumerService.getUtConsumerListByAccount(chjUtConsumer.getAccount());
        //整合list
        if(cbjUtConsumerList.size() > 1){
            //整合所有数据
            rst = cbjUtConsumerList.stream().max(Comparator.comparing(UtConsumer::getLastlogintime)).get();
            rst.setBalance(cbjUtConsumerList.stream().mapToInt(UtConsumer::getBalance).sum());
            rst.setGiveBalance(cbjUtConsumerList.stream().mapToInt(UtConsumer::getGiveBalance).sum());
            rst.setConsumptionAmount(cbjUtConsumerList.stream().filter(e -> e.getConsumptionAmount() != null).mapToInt(UtConsumer::getConsumptionAmount).sum());
            rst.setOrderNum(cbjUtConsumerList.stream().filter(e -> e.getOrderNum() != null).mapToLong(UtConsumer::getOrderNum).sum());
        }else if(cbjUtConsumerList.size() == 1){
            rst = cbjUtConsumerList.get(0);
        }
        return rst;
    }

    /**
     * 检测是否已清洗 - 用户
     * @param utConsumer
     * @param type
     * @return false - 未清洗  true - 已清洗
     */
    @Synchronized
    protected Boolean checkCleanConsumer(UtConsumer utConsumer, int type){
        boolean rst = false;
        if(type == 1) {
            if (consumerLogService.getOneByCbjAccountAndStatusAntType(utConsumer.getAccount(), 1, 1) != null) {
                rst = true;
            }
        }else if(type == 2){
            if (consumerLogService.getOneByChjAccountAndStatusAndType(utConsumer.getAccount(), 1, 1) != null) {
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

    /**
     * 存在多条用户数据(大于2)指向同一用户 叠加余额
     * @param consumerLog
     * @param utConsumer
     * @param type 1:车便捷 2:车惠捷
     */
    protected void updateBalance(ConsumerLog consumerLog, UtConsumer utConsumer, int type){
        //车便捷
        if(type == 1) {
            if (!utConsumer.getId().equals(consumerLog.getCbjId())) {
                List<UtConsumer> utConsumerList = new ArrayList<>();
                utConsumerList.add(cbjUtConsumerService.getUtConsumerById(consumerLog.getCbjId()));
                utConsumerList.add(cbjUtConsumerService.getUtConsumerById(utConsumer.getId()));
                //对比lastlogintime获取最终作为参考的utconsumer
                long rstId = utConsumerList.stream().max(Comparator.comparing(UtConsumer::getLastlogintime)).get().getId();
                //新数据比之前数据都新则合并余额
                if (rstId == utConsumer.getId()) {
                    //更新log - 用户迁移log
                    if (StrUtil.isNotBlank(utConsumer.getUnionid())) {
                        consumerLog.setUnionid(utConsumer.getUnionid());
                    }
                    consumerLog.setCbjId(utConsumer.getId());
                    consumerLog.setCbjAccount(utConsumer.getAccount());
                    consumerLogService.saveOne(consumerLog);
                    //更新log - 用户余额迁移log
                    ConsumerLog consumerBalanceLog = consumerLogService.getOneByConsumerIdAndStatusAndType(consumerLog.getConsumerId(), 1, 2);
                    if (StrUtil.isNotBlank(utConsumer.getUnionid())) {
                        consumerBalanceLog.setUnionid(utConsumer.getUnionid());
                    }
                    consumerBalanceLog.setCbjId(utConsumer.getId());
                    consumerBalanceLog.setCbjAccount(utConsumer.getAccount());
                    consumerLogService.saveOne(consumerBalanceLog);
                    //更新余额
                    ConsumerBalance realConsumerBalance = consumerBalanceService.getByConsumerIdAndBalanceType(consumerLog.getConsumerId(), BalanceType.REAL_BALANCE);
                    realConsumerBalance.setValue(realConsumerBalance.getValue() + utConsumer.getBalance());
                    consumerBalanceService.save(realConsumerBalance);
                    ConsumerBalance giveConsumerBalance = consumerBalanceService.getByConsumerIdAndBalanceType(consumerLog.getConsumerId(), BalanceType.GIVE_BALANCE);
                    giveConsumerBalance.setValue(giveConsumerBalance.getValue() + utConsumer.getGiveBalance());
                    consumerBalanceService.save(giveConsumerBalance);
                }
            }
        }else if (type == 2){
            //车惠捷

        }
    }
}
