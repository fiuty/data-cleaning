package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerBalance;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.enums.BalanceType;

import java.util.List;
import java.util.Map;

public interface ConsumerBalanceService {

    void mergeByConsumer(Consumer consumer, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer);

    List<ConsumerBalance> findByUnionAccount(String unionAccount);

    ConsumerBalance getByConsumerIdAndBalanceType(long consumerId, BalanceType balanceType);

    void  save(ConsumerBalance consumerBalance);

    Map<String, List<ConsumerBalance>> batchFindByUnionAccount(List<String> unionAccounts);
}
