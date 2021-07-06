package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChjUtConsumerService {

    Page<UtConsumer> pageUtConsumer(Pageable pageable);

    UtConsumer getUtConsumerByUnionid(String unionid);

    /**
     * 查询jhi_account字段 (即用户手机号)
     * @param account
     * @return
     */
    UtConsumer getUtConsumerByAccount(String account);

    List<UtConsumer> getUtConsumerListByAccount(String account);

    List<UtConsumer> getUtConsumerListByUnionid(String unionid);


    UtConsumer getUtConsumerByPhone(String phone);
}
