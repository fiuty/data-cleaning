package com.chebianjie.datacleaning.threads;

import com.chebianjie.datacleaning.domain.ConsumerCars;
import com.chebianjie.datacleaning.domain.UtUserCars;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.service.ConsumerCarsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: matingting
 * @Date: 2021/07/02/18:18
 */
@Data
@Slf4j
public class ConsumerCarsThread extends Thread{


   private ConsumerCarsService consumerCarsService;

   private UtUserCars utUserCars;

   private Long consumerId;

   private String phone;

   private String unionAccount;

   private String unionId;

   private Platform platform;


   public ConsumerCarsThread(ConsumerCarsService consumerCarsService, UtUserCars utUserCars, Long consumerId, String phone, String unionAccount, String unionId, Platform platform){
       this.consumerCarsService = consumerCarsService;
       this.utUserCars = utUserCars;
       this.consumerId = consumerId;
       this.phone = phone;
       this.unionAccount = unionAccount;
       this.unionId = unionId;
       this.platform = platform;
    }


    @Override
    public void run(){
        consumerCarsService.saveConsumerCars(utUserCars,consumerId,phone,unionAccount,unionId,platform);
    }




}
