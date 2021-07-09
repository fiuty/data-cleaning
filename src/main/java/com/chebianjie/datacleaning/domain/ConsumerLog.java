package com.chebianjie.datacleaning.domain;

import lombok.Data;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "consumer_log")
@Data
public class ConsumerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * utconsumer_unionid
     */
    private String unionid;

    /**
     * 类型: 1.用户迁移 2.用户余额迁移
     */
    private int type;

    /**
     * 车便捷consumer_id
     */
    private Long cbjId;

    /**
     * 车惠捷consumer_id
     */
    private Long chjId;

    /**
     * 车便捷jhi_account
     */
    private String cbjAccount;

    /**
     * 车惠捷jhi_account
     */
    private String chjAccount;

    /**
     * 状态 0: 失败 1:成功
     */
    private int status;

    /**
     * 迁移后consumer表id
     */
    private long consumerId;
}
