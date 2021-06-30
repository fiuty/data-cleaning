CREATE TABLE `consumer_log`  (
                                 `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `type` tinyint(10) NULL COMMENT '类型: 1.用户迁移 2.用户余额迁移',
                                 `unionid` varchar(255) NULL COMMENT 'utconsumer_unionid',
                                 `cbj_id` bigint NOT NULL COMMENT '车便捷consumer_id',
                                 `chj_id` bigint NOT NULL COMMENT '车惠捷consumer_id',
                                 `cbj_account` varchar(255) NULL COMMENT '车便捷jhi_account',
                                 `chj_account` varchar(255) NULL COMMENT '车惠捷jhi_account',
                                 `status` tinyint(10) NULL COMMENT '状态 0:失败 1:成功',
                                 PRIMARY KEY (`id`)
) COMMENT = '用户迁移表';

