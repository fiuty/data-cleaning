package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumer;

public interface ConsumerBalanceService {

    void merge(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer);
}
