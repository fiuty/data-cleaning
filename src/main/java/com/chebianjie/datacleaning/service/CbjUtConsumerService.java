package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CbjUtConsumerService {

    UtConsumer getUtConsumerById(Long id);

    UtConsumer getUtConsumerByPhone(String phone);

    List<UtConsumer> getUtConsumerListByUnionid(String unionid);

    List<UtConsumer> getUtConsumerListByAccount(String account);

    Page<UtConsumer> pageUtConsumer(Pageable pageable);

    int countByCreatetimeLessThanEqual(Long consumerTime);

    List<UtConsumer> findAllByPage(int pageNumber, int pageSize);
}
