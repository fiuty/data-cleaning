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
     * 设置数据源
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dynamicDataSource(DataSource masterDataSource, DataSource slaveDataSource, DataSource userPlatformDataSource,
                                               DataSource cbjOrderDataSource, DataSource chjOrderDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DynamicDataSource dynamicDataSource = DynamicDataSource.build();
        targetDataSources.put(DataSourcesType.MASTER.name(), masterDataSource);
        targetDataSources.put(DataSourcesType.SLAVE.name(), slaveDataSource);
        targetDataSources.put(DataSourcesType.USERPLATFORM.name(), userPlatformDataSource);
        targetDataSources.put(DataSourcesType.CBJ_ORDER.name(), cbjOrderDataSource);
        targetDataSources.put(DataSourcesType.CHJ_ORDER.name(), chjOrderDataSource);
        //默认数据源配置 DefaultTargetDataSource
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        //额外数据源配置 TargetDataSources
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

}
