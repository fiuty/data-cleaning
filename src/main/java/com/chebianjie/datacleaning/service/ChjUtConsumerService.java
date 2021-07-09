package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChjUtConsumerService {

    UtConsumer getUtConsumerById(Long id);

    UtConsumer getUtConsumerByUnionid(String unionid);

    UtConsumer getUtConsumerByAccount(String account);

    UtConsumer getUtConsumerByPhone(String phone);

    List<UtConsumer> getUtConsumerListByAccount(String account);

    List<UtConsumer> getUtConsumerListByUnionid(String unionid);

    Page<UtConsumer> pageUtConsumer(Pageable pageable);
}
