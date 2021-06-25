package com.chebianjie.datacleaning.common.annotation;

import com.chebianjie.datacleaning.common.enums.DataSourcesType;

import java.lang.annotation.*;

/**
 * 数据源自定义注解
 * @author Administrator
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {

    DataSourcesType name() default DataSourcesType.MASTER;

}
