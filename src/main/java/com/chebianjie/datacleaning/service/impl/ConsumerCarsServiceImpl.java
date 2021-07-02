package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.*;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.*;
import com.chebianjie.datacleaning.service.ConsumerCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: matingting
 * @Date: 2021/06/25/18:25
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerCarsServiceImpl implements ConsumerCarsService {

//    @Autowired
//    ConsumerCarMapper consumerCarMapper;


    @Autowired
    ConsumerRepository consumerRepository;

    @Autowired
    UtConsumerRepository utConsumerRepository;

    @Autowired
    UtUserCarsRepository utUserCarsRepository;

    @Autowired
    ConsumerCarsRepository consumerCarsRepository;

    @Autowired
    ConsumerLogRepository consumerLogRepository;

    @Autowired
    ConsumerCarsLogRepository consumerCarsLogRepository;


    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public Page<UtUserCars> getUtUserCarsByCbj(Pageable pageable) {
        return utUserCarsRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public Page<UtUserCars> getUtUserCarsByChj(Pageable pageable) {
        return utUserCarsRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtUserCars> getChjUtUserCarsByUid(Long Uid) {
        return utUserCarsRepository.findAllByUid(Uid);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public void  saveConsumerCars(UtUserCars userCars,String phone,String unionAccount,String unionId,Platform platform){
        try {

        ConsumerCars consumerCars;
        if (userCars != null) {

            consumerCars = toConsumerCars(userCars, platform);
            consumerCars.setUnionAccount(unionAccount);
            consumerCarsRepository.save(consumerCars);

            //记录成功日志
            ConsumerCarsLog consumerCarsLog=new ConsumerCarsLog();
            consumerCarsLog.setUserCarsId(userCars.getId());
            consumerCarsLog.setUid(userCars.getUid());
            consumerCarsLog.setUnionId(unionId);
            consumerCarsLog.setAccount(phone);
            consumerCarsLog.setUnionAccount(unionAccount);
            consumerCarsLog.setStatus(1);
            consumerCarsLog.setPlatform(platform);
            consumerCarsLog.setCreatTime(LocalDateTime.now());
            consumerCarsLogRepository.save(consumerCarsLog);
        }

        }catch (Exception e){
            //记录失败log
            ConsumerCarsLog consumerCarsLog=new ConsumerCarsLog();
            consumerCarsLog.setUserCarsId(userCars.getId());
            consumerCarsLog.setUid(userCars.getUid());
            consumerCarsLog.setAccount(phone);
            consumerCarsLog.setUnionAccount(unionAccount);
            consumerCarsLog.setStatus(0);
            consumerCarsLog.setPlatform(platform);
            consumerCarsLog.setCreatTime(LocalDateTime.now());
            consumerCarsLogRepository.save(consumerCarsLog);

        }

    }




    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer getCbjUtConsumerById(Long Uid) {
        return utConsumerRepository.findById(Uid).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer getChjUtConsumerById(Long Uid) {
        return utConsumerRepository.findById(Uid).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer getConsumerByPhone(String phone) {
        return consumerRepository.findByPhone(phone);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer getConsumerByUnionid(String unionid) {
        return consumerRepository.findByWechatUnionId(unionid);
    }






    public ConsumerCars toConsumerCars(UtUserCars item,Platform platform){
        ConsumerCars car=new ConsumerCars();
        car.setConsumerId(item.getUid());
        car.setBrand(item.getBrand());
        car.setBrandId(item.getBrandId());
        car.setCarSystem(item.getCarSystem());
        car.setCarSystemId(item.getCarSystemId());
        car.setModel(item.getModel());
        car.setModelId(item.getModelId());
        car.setPurchaseTime(item.getPurchaseTime());
        car.setDistance(item.getDistance());
        car.setCarNum(item.getCarNum());
        car.setCarFrameNum(item.getCarFrameNum());
        car.setEngineNum(item.getEngineNum());
        car.setInsuranceCompan(item.getInsuranceCompan());
        car.setExpireTime(item.getExpireTime());
        car.setCarOwner(item.getCarOwner());
        car.setValidate(item.getJhiValidate());
        car.setIsDefault(item.getIsDefault());
        car.setLogo(item.getLogo());
        car.setPhotoUrlFront(item.getPhotoUrlFront());
        car.setPhotoUrlBack(item.getPhotoUrlBack());
        car.setLevel(item.getJhiLevel());
        car.setArea(item.getArea());
        car.setInsuranceType(item.getInsuranceType());
        car.setCity(item.getCity());
        car.setUpdateTime(item.getUpdatetime());
        car.setNickname(item.getNickname());
        car.setRealName(item.getRealname());
        car.setIdCard(item.getIdCard());
        car.setCarType(item.getCarType());
        car.setAccidentType(item.getAccidentType());
        car.setPlatform(platform);
        //car.setUnionAccount(item.getUnionAccount());  //消费者唯一账号

        return car;
    }







}
