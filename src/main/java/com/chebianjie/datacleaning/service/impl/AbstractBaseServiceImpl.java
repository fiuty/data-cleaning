package com.chebianjie.datacleaning.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.repository.ConsumerBalanceRepository;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractBaseServiceImpl {

    @Autowired
    protected ConsumerBalanceRepository consumerBalanceRepository;

    @Autowired
    protected ConsumerLogRepository consumerLogRepository;

    /**
     * 根据入参获取已迁移新用户失败记录
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @param type 1:清洗用户 2:清洗余额
     * @return
     */
    protected ConsumerLog getFailConsumerLog(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer, int type){
        ConsumerLog rst;
        int status = 0;
        if (chjUtConsumer == null && cbjUtConsumer != null) {
            //1. 车惠捷数据null 以车便捷数据入库
            rst = consumerLogRepository.findOneByCbjAccountAndStatusAndType(cbjUtConsumer.getAccount(), status, type);
        }else if(chjUtConsumer != null && cbjUtConsumer == null){
            //2. 车便捷数据null 以车惠捷数据入库
            rst = consumerLogRepository.findOneByChjAccountAndStatusAndType(chjUtConsumer.getAccount(), status, type);
        }else {
            //3. 两者不为空, 对比注册时间, 以较早注册的为主
            if(cbjUtConsumer.getLastlogintime() > chjUtConsumer.getLastlogintime()) {
                rst = consumerLogRepository.findOneByCbjAccountAndStatusAndType(cbjUtConsumer.getAccount(), status, type);
            }else{
                rst = consumerLogRepository.findOneByChjAccountAndStatusAndType(chjUtConsumer.getAccount(), status, type);
            }
        }
        return rst;
    }

    /**
     * 根据入参筛选unionid
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @return
     */
    protected String fixConsumerLogUnionid(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        String rst = "";
        if (chjUtConsumer == null && cbjUtConsumer != null) {
            //1. 车惠捷数据null 以车便捷数据入库
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
                rst = cbjUtConsumer.getUnionid();
            }
        }else if(chjUtConsumer != null && cbjUtConsumer == null){
            //2. 车便捷数据null 以车惠捷数据入库
            if(StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = chjUtConsumer.getUnionid();
            }
        }else {
            //3. 两者不为空, 对比注册时间, 以较早注册的为主
            if (cbjUtConsumer.getLastlogintime() > chjUtConsumer.getLastlogintime() && StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
                rst = cbjUtConsumer.getUnionid();
            }else if(chjUtConsumer.getLastlogintime() > cbjUtConsumer.getLastlogintime() && StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = chjUtConsumer.getUnionid();
            }
        }
        return rst;
    }

    /**
     * 根据入参生成清洗日志
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @param type 1:清洗用户 2:清洗用户余额
     * @param status 1:成功 0:失败
     * @return
     */
    protected ConsumerLog generateConsumerLog(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer, int type, int status){
        ConsumerLog temp = new ConsumerLog();
        temp.setUnionid(fixConsumerLogUnionid(cbjUtConsumer, chjUtConsumer));
        if (cbjUtConsumer != null) {
            temp.setCbjId(cbjUtConsumer.getId());
        }
        if (chjUtConsumer != null) {
            temp.setChjId(chjUtConsumer.getId());
        }
        if (cbjUtConsumer != null && StrUtil.isNotBlank(cbjUtConsumer.getAccount())) {
            temp.setCbjAccount(cbjUtConsumer.getAccount());
        }
        if (chjUtConsumer != null && StrUtil.isNotBlank(chjUtConsumer.getAccount())) {
            temp.setChjAccount(chjUtConsumer.getAccount());
        }
        temp.setType(type);
        temp.setStatus(status);
        return temp;
    }
}
