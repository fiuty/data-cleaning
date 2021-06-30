package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
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
}
