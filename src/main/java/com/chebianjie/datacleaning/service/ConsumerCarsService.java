package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerCars;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.UtUserCars;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.UtUserCarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;

/**
 * @Author: matingting
 * @Date: 2021/06/25/18:14
 */
public interface ConsumerCarsService {


    public Page<UtUserCars> getUtUserCarsByCbj(Pageable pageable);

    public Page<UtUserCars> getUtUserCarsByChj(Pageable pageable);

    public List<UtUserCars> getChjUtUserCarsByUid(Long Uid);

    public void  saveConsumerCars(UtUserCars utUserCars, String phone, String unionAccount,String unionId, Platform platform);

    //车便捷
    public UtConsumer getCbjUtConsumerById(Long Uid);

    //车惠捷
    public UtConsumer getChjUtConsumerById(Long Uid);

    //用户中心
    public Consumer getConsumerByPhone(String phone);

    //用户中心
    public Consumer getConsumerByUnionid(String unionid);





}
