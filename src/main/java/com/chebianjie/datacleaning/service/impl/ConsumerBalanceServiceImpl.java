package com.chebianjie.datacleaning.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerBalance;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.enums.BalanceType;
import com.chebianjie.datacleaning.repository.ConsumerBalanceRepository;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import com.chebianjie.datacleaning.repository.ConsumerRepository;
import com.chebianjie.datacleaning.service.ConsumerBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerBalanceServiceImpl implements ConsumerBalanceService {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerBalanceRepository consumerBalanceRepository;

    @Autowired
    private ConsumerLogRepository consumerLogRepository;

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void merge(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer) {
        try {
            ConsumerBalance consumerRealBalance;
            ConsumerBalance consumerGiveBalance;
            Consumer consumer = getConsumer(cbjUtConsumer, chjUtConsumer);
            if (chjUtConsumer == null && cbjUtConsumer != null) {
                //1. 车惠捷数据null 以车便捷数据入库
                consumerRealBalance = transConsumerBalance(consumer, BalanceType.REAL_BALANCE, cbjUtConsumer, null);
                consumerGiveBalance = transConsumerBalance(consumer, BalanceType.GIVE_BALANCE, cbjUtConsumer, null);
            }else if(chjUtConsumer != null && cbjUtConsumer == null){
                consumerRealBalance = transConsumerBalance(consumer, BalanceType.REAL_BALANCE, null, chjUtConsumer);
                consumerGiveBalance = transConsumerBalance(consumer, BalanceType.GIVE_BALANCE, null, chjUtConsumer);
            }else {
                //2. 两者不为空, 对比注册时间, 以较早注册的为主
                consumerRealBalance = transConsumerBalance(consumer, BalanceType.REAL_BALANCE, cbjUtConsumer, chjUtConsumer);
                consumerGiveBalance = transConsumerBalance(consumer, BalanceType.GIVE_BALANCE, cbjUtConsumer, chjUtConsumer);
            }
            consumerBalanceRepository.save(consumerRealBalance);
            consumerBalanceRepository.save(consumerGiveBalance);
            //3.迁移成功记录日志
            //判断是否有失败记录, 有则更新 无则插入
            ConsumerLog curConsumerLog = getConsumerLog(cbjUtConsumer, chjUtConsumer);
            if(curConsumerLog != null) {
                curConsumerLog.setStatus(1);
                consumerLogRepository.save(curConsumerLog);
            }else{
                ConsumerLog temp = new ConsumerLog();
                if (StrUtil.isNotBlank(cbjUtConsumer.getUnionid())) {
                    temp.setUnionid(cbjUtConsumer.getUnionid());
                }
                temp.setCbjId(cbjUtConsumer.getId());
                if (chjUtConsumer != null) {
                    temp.setChjId(chjUtConsumer.getId());
                }
                if (StrUtil.isNotBlank(cbjUtConsumer.getAccount())) {
                    temp.setCbjAccount(cbjUtConsumer.getAccount());
                }
                if (chjUtConsumer != null && StrUtil.isNotBlank(chjUtConsumer.getAccount())) {
                    temp.setChjAccount(chjUtConsumer.getAccount());
                }
                temp.setType(2);
                temp.setStatus(1);
                consumerLogRepository.save(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
            //4. 有任何报错记录log 失败
            ConsumerLog temp = new ConsumerLog();
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
                temp.setUnionid(cbjUtConsumer.getUnionid());
            }
            temp.setCbjId(cbjUtConsumer.getId());
            if(chjUtConsumer != null){
                temp.setChjId(chjUtConsumer.getId());
            }
            if(StrUtil.isNotBlank(cbjUtConsumer.getAccount())){
                temp.setCbjAccount(cbjUtConsumer.getAccount());
            }
            if(chjUtConsumer != null && StrUtil.isNotBlank(chjUtConsumer.getAccount())){
                temp.setChjAccount(chjUtConsumer.getAccount());
            }
            temp.setType(2);
            temp.setStatus(0);
            consumerLogRepository.save(temp);
        }
    }

    /**
     * 根据入参获取以迁移新用户信息
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @return
     */
    private Consumer getConsumer(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        Consumer rst;
        if (chjUtConsumer == null && cbjUtConsumer != null) {
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
                rst = consumerRepository.findByWechatUnionId(cbjUtConsumer.getUnionid());
            }else{
                rst = consumerRepository.findByPhone(cbjUtConsumer.getAccount());
            }
        }else if(chjUtConsumer != null && cbjUtConsumer == null){
            if(StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = consumerRepository.findByWechatUnionId(chjUtConsumer.getUnionid());
            }else{
                rst = consumerRepository.findByPhone(chjUtConsumer.getAccount());
            }
        }else {
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid()) || StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = consumerRepository.findByWechatUnionId(cbjUtConsumer.getUnionid());
            }else{
                rst = consumerRepository.findByPhone(cbjUtConsumer.getCreatetime() < chjUtConsumer.getCreatetime() ? cbjUtConsumer.getAccount() : chjUtConsumer.getAccount());
            }
        }
        return rst;
    }

    /**
     * 根据入参获取以迁移新用户记录
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @return
     */
    private ConsumerLog getConsumerLog(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        ConsumerLog rst;
        if (chjUtConsumer == null && cbjUtConsumer != null) {
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid())){
                rst = consumerLogRepository.findOneByUnionidAndStatusAndType(cbjUtConsumer.getUnionid(), 0, 2);
            }else{
                rst = consumerLogRepository.findOneByCbjIdAndStatusAndType(cbjUtConsumer.getId(), 0, 2);
            }
        }else if(chjUtConsumer != null && cbjUtConsumer == null){
            if(StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = consumerLogRepository.findOneByUnionidAndStatusAndType(chjUtConsumer.getUnionid(), 0, 2);
            }else{
                rst = consumerLogRepository.findOneByChjIdAndStatusAndType(chjUtConsumer.getId(), 0, 2);
            }
        }else {
            if(StrUtil.isNotBlank(cbjUtConsumer.getUnionid()) || StrUtil.isNotBlank(chjUtConsumer.getUnionid())){
                rst = consumerLogRepository.findOneByUnionidAndStatusAndType(cbjUtConsumer.getUnionid(), 0, 2);
            }else{
                if(cbjUtConsumer.getCreatetime() < chjUtConsumer.getCreatetime()) {
                    rst = consumerLogRepository.findOneByCbjIdAndStatusAndType(cbjUtConsumer.getId(), 0, 2);
                }else{
                    rst = consumerLogRepository.findOneByChjIdAndStatusAndType(chjUtConsumer.getId(), 0, 2);
                }
            }
        }
        return rst;
    }

    /**
     * 产生新用户余额数据
     * @param consumer
     * @param balanceType
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @return
     */
    private ConsumerBalance transConsumerBalance(Consumer consumer, BalanceType balanceType, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        ConsumerBalance rst = new ConsumerBalance();

        rst.setUnionAccount(consumer.getUnionAccount());
        rst.setConsumerId(consumer.getId());
        //大客户没正式数据暂不处理
        //rst.setBusinessId();
        rst.setBalanceType(balanceType);
        //余额 以分为单位
        rst.setValue(fixBalanceValue(balanceType, cbjUtConsumer, chjUtConsumer));

        return rst;
    }

    /**
     * 根据入参计算余额数值
     * @param balanceType
     * @param cbjUtConsumer
     * @param chjUtConsumer
     * @return
     */
    private int fixBalanceValue(BalanceType balanceType, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer){
        int rst;
        switch (balanceType){
            case REAL_BALANCE:
                rst = (cbjUtConsumer == null ? 0 : cbjUtConsumer.getBalance()) + (chjUtConsumer == null ? 0 : chjUtConsumer.getBalance());
                break;
            case GIVE_BALANCE:
                rst = (cbjUtConsumer == null ? 0 : cbjUtConsumer.getGiveBalance()) + (chjUtConsumer == null ? 0 : chjUtConsumer.getGiveBalance());
                break;
            default:
                rst = 0;
                break;
        }
        return rst;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<ConsumerBalance> findByUnionAccount(String unionAccount) {
        return consumerBalanceRepository.findByUnionAccount(unionAccount);
    }
}
