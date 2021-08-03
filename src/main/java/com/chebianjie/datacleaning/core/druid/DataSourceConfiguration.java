package com.chebianjie.datacleaning.core.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.datasource.DynamicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置类
 */
@Configuration
public class DataSourceConfiguration {
    /**
     * 主库
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }


    /**
     * 从库
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.slave", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DataSource slaveDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 用户合并
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.userplatform", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.userplatform")
    public DataSource userPlatformDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 车便捷订单
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.cbjorder", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.cbjorder")
    public DataSource cbjOrderDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 车惠捷订单
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.chjorder", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.chjorder")
    public DataSource chjOrderDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }



    /**
     * 车便捷优惠券
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.cbjcoupon", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.cbjcoupon")
    public DataSource cbjCouponDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 车惠捷优惠券
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.chjcoupon", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.chjcoupon")
    public DataSource chjCouponDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }


    /**
     * 车便捷staff
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.cbjstaff", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.cbjstaff")
    public DataSource cbjStaffDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 车惠捷staff
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.chjstaff", name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.chjstaff")
    public DataSource chjStaffDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }


    /**
     * 车便捷agent
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.cbjagent",name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.cbjagent")
    public DataSource cbjAgentDataSource(DataSourceProperties dataSourceProperties){
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 车惠捷agent
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.chjagent",name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.chjagent")
    public DataSource chjAgentDataSource(DataSourceProperties dataSourceProperties){
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }


    /**
     * 车便捷carserver
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.cbjcarserver",name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.cbjcarserver")
    public DataSource cbjCarServerDataSource(DataSourceProperties dataSourceProperties){
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }




    /**
     * 车便捷report
     * @param dataSourceProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.cbjreport",name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.cbjreport")
    public DataSource cbjReportDataSource(DataSourceProperties dataSourceProperties){
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }


    /**
     * 车惠捷report
     * @param dataSourceProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty( prefix = "spring.datasource.druid.chjreport",name = "enable", havingValue = "true")//是否开启数据源开关---若不开启 默认适用默认数据源
    @ConfigurationProperties("spring.datasource.druid.chjreport")
    public DataSource chjReportDataSource(DataSourceProperties dataSourceProperties){
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }






    /**
     * 设置数据源
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dynamicDataSource(DataSource masterDataSource, DataSource slaveDataSource, DataSource userPlatformDataSource,
                                               DataSource cbjOrderDataSource, DataSource chjOrderDataSource, DataSource cbjCouponDataSource,
                                               DataSource chjCouponDataSource, DataSource cbjStaffDataSource, DataSource chjStaffDataSource,
                                               DataSource cbjAgentDataSource,DataSource chjAgentDataSource,DataSource cbjCarServerDataSource,
                                               DataSource cbjReportDataSource,DataSource chjReportDataSource
                                               ) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DynamicDataSource dynamicDataSource = DynamicDataSource.build();
        targetDataSources.put(DataSourcesType.MASTER.name(), masterDataSource);
        targetDataSources.put(DataSourcesType.SLAVE.name(), slaveDataSource);
        targetDataSources.put(DataSourcesType.USERPLATFORM.name(), userPlatformDataSource);
        targetDataSources.put(DataSourcesType.CBJ_ORDER.name(), cbjOrderDataSource);
        targetDataSources.put(DataSourcesType.CHJ_ORDER.name(), chjOrderDataSource);
        targetDataSources.put(DataSourcesType.CBJ_COUPON.name(), cbjCouponDataSource);
        targetDataSources.put(DataSourcesType.CHJ_COUPON.name(), chjCouponDataSource);
        targetDataSources.put(DataSourcesType.CBJ_STAFF.name(), cbjStaffDataSource);
        targetDataSources.put(DataSourcesType.CHJ_STAFF.name(), chjStaffDataSource);
        targetDataSources.put(DataSourcesType.CBJ_AGENT.name(), cbjAgentDataSource);
        targetDataSources.put(DataSourcesType.CHJ_AGENT.name(), chjAgentDataSource);
        targetDataSources.put(DataSourcesType.CBJ_CAR_SERVER.name(), cbjCarServerDataSource);
        targetDataSources.put(DataSourcesType.CBJ_REPORT.name(), cbjReportDataSource);
        targetDataSources.put(DataSourcesType.CHJ_REPORT.name(), chjReportDataSource);

        //默认数据源配置 DefaultTargetDataSource
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        //额外数据源配置 TargetDataSources
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

}
