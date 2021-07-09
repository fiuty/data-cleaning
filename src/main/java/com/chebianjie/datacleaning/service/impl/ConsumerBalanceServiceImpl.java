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
public class ConsumerBalanceServiceImpl extends AbstractBaseServiceImpl implements ConsumerBalanceService {

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void mergeByConsumer(Consumer consumer, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer) {
        try {
            //真实余额
            ConsumerBalance consumerRealBalance;
            //赠送余额
            ConsumerBalance consumerGiveBalance;
            if (chjUtConsumer == null && cbjUtConsumer != null) {
                //1. 车惠捷数据null 以车便捷数据入库
                consumerRealBalance = transConsumerBalance(consumer, BalanceType.REAL_BALANCE, cbjUtConsumer, null);
                consumerGiveBalance = transConsumerBalance(consumer, BalanceType.GIVE_BALANCE, cbjUtConsumer, null);
            }else if(chjUtConsumer != null && cbjUtConsumer == null){
                //2. 车便捷数据null 以车惠捷数据入库
                consumerRealBalance = transConsumerBalance(consumer, BalanceType.REAL_BALANCE, null, chjUtConsumer);
                consumerGiveBalance = transConsumerBalance(consumer, BalanceType.GIVE_BALANCE, null, chjUtConsumer);
            }else {
                //3. 两者不为空, 累加
                consumerRealBalance = transConsumerBalance(consumer, BalanceType.REAL_BALANCE, cbjUtConsumer, chjUtConsumer);
                consumerGiveBalance = transConsumerBalance(consumer, BalanceType.GIVE_BALANCE, cbjUtConsumer, chjUtConsumer);
            }
            consumerBalanceRepository.save(consumerRealBalance);
            consumerBalanceRepository.save(consumerGiveBalance);
            //4.迁移成功记录日志 -判断是否有失败记录, 有则更新 无则插入
            ConsumerLog curConsumerLog = getFailConsumerLog(cbjUtConsumer, chjUtConsumer, 2);
            if(curConsumerLog != null) {
                curConsumerLog.setConsumerId(consumer.getId());
                curConsumerLog.setStatus(1);
                consumerLogRepository.save(curConsumerLog);
            }else{
                ConsumerLog temp = generateConsumerLog(cbjUtConsumer, chjUtConsumer, 2, 1);
                temp.setConsumerId(consumer.getId());
                consumerLogRepository.save(temp);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            //5. 有任何报错记录log 失败
            ConsumerLog temp = generateConsumerLog(cbjUtConsumer, chjUtConsumer, 2, 0);
            consumerLogRepository.save(temp);
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<ConsumerBalance> findByUnionAccount(String unionAccount) {
        return consumerBalanceRepository.findByUnionAccount(unionAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public ConsumerBalance getByConsumerIdAndBalanceType(long consumerId, BalanceType balanceType) {
        return consumerBalanceRepository.findOneByConsumerIdAndBalanceType(consumerId, balanceType);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void save(ConsumerBalance consumerBalance) {
        consumerBalanceRepository.save(consumerBalance);
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
}
