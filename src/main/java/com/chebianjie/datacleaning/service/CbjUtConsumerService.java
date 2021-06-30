package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CbjUtConsumerService {

    Page<UtConsumer> pageUtConsumer(Pageable pageable);

    UtConsumer getUtConsumerById(Long id);

    List<UtConsumer> listByUnionid(String unionid);
}
