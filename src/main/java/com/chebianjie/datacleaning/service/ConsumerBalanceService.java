package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerBalance;
import com.chebianjie.datacleaning.domain.UtConsumer;

import java.util.List;

public interface ConsumerBalanceService {

    void merge(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer);

    void mergeByConsumer(Consumer consumer, UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer);

    List<ConsumerBalance> findByUnionAccount(String unionAccount);
}
