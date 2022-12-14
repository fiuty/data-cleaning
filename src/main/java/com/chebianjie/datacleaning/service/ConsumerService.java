package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-25
 */
public interface ConsumerService {

    UtConsumer listMster();

    UtConsumer listSlave();

    Consumer mergeConsumer(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer);

    int countByRegistryTimeLessThanEqual(LocalDateTime flowConsumerTime);

    List<Consumer> findAllByPage(int pageNumber, int pageSize);

    Consumer findById(Long id);

    List<Consumer> findAllByIdIn(List<Long> ids);

    Consumer getByConsumerLog(ConsumerLog consumerLog);

    Long findTotalCount();

    Consumer findByWechatUnionId(String wechatUnionId);

    Consumer findByPhone(String phone);

    Consumer findByUnionAccount(String unionAccount);

    List<Consumer> findByUnionAccountIn(List<String> unionAccounts);
}
