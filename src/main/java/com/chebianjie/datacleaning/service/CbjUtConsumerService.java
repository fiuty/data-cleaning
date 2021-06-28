package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CbjUtConsumerService {

    Page<UtConsumer> pageUtConsumer(Pageable pageable);

    UtConsumer getUtConsumerById(Long id);
}
