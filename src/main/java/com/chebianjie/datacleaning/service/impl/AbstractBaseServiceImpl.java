package com.chebianjie.datacleaning.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractBaseServiceImpl {

    @Autowired
    protected ConsumerLogRepository consumerLogRepository;

    /**
     * 根据入参获取以迁移新用户记录
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @return
     */
    protected ConsumerLog getConsumerLog(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        ConsumerLog rst;
        if (chjUtConsumer == null && cbjUtConsumer != null) {
            //1. 车惠捷数据null 以车便捷数据入库
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
                rst = consumerLogRepository.findOneByUnionidAndStatusAndType(cbjUtConsumer.getUnionid(), 0, 1);
            }else{
                rst = consumerLogRepository.findOneByCbjIdAndStatusAndType(cbjUtConsumer.getId(), 0, 1);
            }
        }else if(chjUtConsumer != null && cbjUtConsumer == null){
            //2. 车便捷数据null 以车惠捷数据入库
            if(StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = consumerLogRepository.findOneByUnionidAndStatusAndType(chjUtConsumer.getUnionid(), 0, 1);
            }else{
                rst = consumerLogRepository.findOneByChjIdAndStatusAndType(chjUtConsumer.getId(), 0, 1);
            }
        }else {
            //3. 两者不为空, 对比注册时间, 以较早注册的为主
            if(cbjUtConsumer.getCreatetime() < chjUtConsumer.getCreatetime()) {
                rst = consumerLogRepository.findOneByCbjIdAndStatusAndType(cbjUtConsumer.getId(), 0, 1);
            }else{
                rst = consumerLogRepository.findOneByChjIdAndStatusAndType(chjUtConsumer.getId(), 0, 1);
            }
        }
        return rst;
    }

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
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid()) && StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                if (cbjUtConsumer.getCreatetime() < chjUtConsumer.getCreatetime()){
                    rst = cbjUtConsumer.getUnionid();
                }else{
                    rst = chjUtConsumer.getUnionid();
                }
            }
        }
        return rst;
    }
}
