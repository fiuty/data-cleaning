package com.chebianjie.datacleaning.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.Consumer;
import com.chebianjie.datacleaning.domain.ConsumerLog;
import com.chebianjie.datacleaning.domain.UtConsumer;
import com.chebianjie.datacleaning.domain.enums.ClientType;
import com.chebianjie.datacleaning.domain.enums.ConsumerStatus;
import com.chebianjie.datacleaning.domain.enums.Gender;
import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.repository.ConsumerLogRepository;
import com.chebianjie.datacleaning.repository.ConsumerRepository;
import com.chebianjie.datacleaning.repository.UtConsumerRepository;
import com.chebianjie.datacleaning.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

/**
 * @author zhengdayue
 * @date: 2021-06-25
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConsumerServiceImpl extends AbstractBaseServiceImpl implements ConsumerService {

    @Autowired
    private UtConsumerRepository utConsumerRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerLogRepository consumerLogRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public UtConsumer listMster() {
        UtConsumer utConsumer = utConsumerRepository.findById(10L).orElse(null);
        log.info("utConsumer:{}",utConsumer.getNickname());
        return null;
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public UtConsumer listSlave() {
        UtConsumer utConsumer = utConsumerRepository.findById(131086L).orElse(null);
        log.info("utConsumer:{}",utConsumer.getNickname());
        return null;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer mergeConsumer(UtConsumer cbjUtConsumer, UtConsumer chjUtConsumer) {
        Consumer consumer = null;
        try {
            if (chjUtConsumer == null && cbjUtConsumer != null) {
                //1. 车惠捷数据null 以车便捷数据入库
                consumer = transConsumer(cbjUtConsumer, 1);
            }else if(chjUtConsumer != null && cbjUtConsumer == null){
                //2. 车便捷数据null 以车惠捷数据入库
                consumer = transConsumer(chjUtConsumer, 2);
            }else {
                //3. 两者不为空, 对比最后登陆时间, 以最近的为主
                if (cbjUtConsumer.getLastlogintime() > chjUtConsumer.getLastlogintime()){
                    consumer = transConsumer(cbjUtConsumer, 1);
                }else{
                    consumer = transConsumer(chjUtConsumer, 2);
                }
                //4. 处理积分
                fixIntegral(consumer, cbjUtConsumer, chjUtConsumer);
            }
            consumerRepository.save(consumer);
            //5.迁移成功记录日志 - 判断是否有失败记录, 有则更新 无则插入
            ConsumerLog curConsumerLog = getFailConsumerLog(cbjUtConsumer , chjUtConsumer, 1);
            if(curConsumerLog != null) {
                curConsumerLog.setConsumerId(consumer.getId());
                curConsumerLog.setStatus(1);
                consumerLogRepository.save(curConsumerLog);
            }else{
                ConsumerLog temp = generateConsumerLog(cbjUtConsumer, chjUtConsumer, 1, 1);
                temp.setConsumerId(consumer.getId());
                consumerLogRepository.save(temp);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            //6. 有任何报错记录log 失败
            ConsumerLog temp = generateConsumerLog(cbjUtConsumer, chjUtConsumer, 1, 0);
            consumerLogRepository.save(temp);
        }
        return consumer;
    }

    /**
     * 转旧consumer为新consumer
     * @param utConsumer 旧consumer
     * @param platform 1:车便捷 2:车惠捷
     * @return
     */
    private Consumer transConsumer(UtConsumer utConsumer, int platform){
        Consumer rst = new Consumer();

        rst.setUnionAccount(UUID.randomUUID().toString().replaceAll("-", ""));
        rst.setNickname(StrUtil.isNotBlank(utConsumer.getNickname()) ? utConsumer.getNickname() : null);
        rst.setAvatar(StrUtil.isNotBlank(utConsumer.getAvatar()) ? utConsumer.getAvatar() : null);
        rst.setRegistryTime(utConsumer.getCreatetime() != null ? LocalDateTime.ofEpochSecond(utConsumer.getCreatetime()/1000, 0, ZoneOffset.ofHours(8)) : null);
        rst.setRegistryPlatform(platform == 1 ? Platform.CHEBIANJIE : Platform.CHEHUIJIE);
        rst.setClientType(utConsumer.getDataFrom() != null ? ClientType.fixClientType(utConsumer.getDataFrom()) : null);
        //以jhi_account为准确手机号
        rst.setPhone(StrUtil.isNotBlank(utConsumer.getAccount()) ? utConsumer.getAccount() : null);
        rst.setLoginPassword(StrUtil.isNotBlank(utConsumer.getPwd()) ? utConsumer.getPwd() : null);
        rst.setPayPassword(StrUtil.isNotBlank(utConsumer.getPwd2()) ? utConsumer.getPwd2() : null);
        rst.setRealName(StrUtil.isNotBlank(utConsumer.getRealname()) ? utConsumer.getRealname() : null);
        rst.setReferrer(utConsumer.getReferrer() != null ? utConsumer.getReferrer() + "" : null);
        rst.setStatus(utConsumer.getStatue() != null ? ConsumerStatus.fixConsumerStatus(utConsumer.getStatue()) : null);
        rst.setWechatMpOpenId(StrUtil.isNotBlank(utConsumer.getOpenid()) ? utConsumer.getOpenid() : null);
        rst.setWechatMiniAppOpenId(StrUtil.isNotBlank(utConsumer.getMiniappOpenid()) ? utConsumer.getMiniappOpenid() : null);
        rst.setWechatUnionId(StrUtil.isNotBlank(utConsumer.getUnionid()) ? utConsumer.getUnionid() : null);
        rst.setGender(utConsumer.getSex() != null ? Gender.fixGender(utConsumer.getSex()) : null);
        rst.setBirthday(utConsumer.getBirthday());
        rst.setBindSiteId(utConsumer.getStairSiteId());
        rst.setBindSiteName(utConsumer.getStairSiteId() != null ? utConsumer.getStairSiteName() : null);
        rst.setBindSitePlatform(fixBindSitePlatform(utConsumer, platform));
        rst.setBindSiteTime(fixBindSiteTime(utConsumer));
        rst.setConsumptionNum(utConsumer.getOrderNum() != null ? utConsumer.getOrderNum().intValue() : null);
        rst.setConsumptionPrice(utConsumer.getConsumptionAmount());
        rst.setLastLoginTime(utConsumer.getLastlogintime() != null ? LocalDateTime.ofEpochSecond(utConsumer.getLastlogintime()/1000, 0, ZoneOffset.ofHours(8)) : null);
        rst.setIntegral(utConsumer.getIntegral() != null ? utConsumer.getIntegral() : 0);

        return rst;
    }

    /**
     * 处理 bindSitePlatform
     * @param utConsumer
     * @param platform
     * @return
     */
    private Platform fixBindSitePlatform(UtConsumer utConsumer, int platform){
        if(utConsumer.getStairSiteId() == null){
            return null;
        }else{
            return platform == 1 ? Platform.CHEBIANJIE : Platform.CHEHUIJIE;
        }
    }

    /**
     * 处理 bindSiteTime
     * @param utConsumer
     * @return
     */
    private LocalDateTime fixBindSiteTime(UtConsumer utConsumer){
        if(utConsumer.getStairSiteId() != null && utConsumer.getStairSiteBindingTime() != null){
            return LocalDateTime.ofEpochSecond(utConsumer.getStairSiteBindingTime()/1000, 0, ZoneOffset.ofHours(8));
        }else{
            return null;
        }
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public int countByRegistryTimeLessThanEqual(LocalDateTime flowConsumerTime) {
        return consumerRepository.countByRegistryTimeLessThanEqual(flowConsumerTime);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<Consumer> findAllByPage(int pageNumber, int pageSize) {
        return consumerRepository.findAllByPage(pageNumber, pageSize);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer findById(Long id) {
        return consumerRepository.findById(id).orElse(null);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<Consumer> findAllByIdIn(List<Long> ids) {
        return consumerRepository.findAllById(ids);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer getByConsumerLog(ConsumerLog consumerLog) {
        Consumer rst = null;
        log.info("[consumerLog]: {}", consumerLog.getId());
        rst = consumerRepository.findById(consumerLog.getConsumerId()).orElse(null);
        return rst;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer findByWechatUnionId(String wechatUnionId) {
        return consumerRepository.findByWechatUnionId(wechatUnionId);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer findByPhone(String phone) {
        return consumerRepository.findByPhone(phone);
    }


    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Long findTotalCount() {
        Long totalConsumer = consumerRepository.findTotalConsumer();
        return totalConsumer;
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public Consumer findByUnionAccount(String unionAccount) {
        return consumerRepository.findByUnionAccount(unionAccount);
    }

    @Override
    @DataSource(name = DataSourcesType.USERPLATFORM)
    public List<Consumer> findByUnionAccountIn(List<String> unionAccounts) {
        return consumerRepository.findAllByUnionAccountIn(unionAccounts);
    }

}
