package com.chebianjie.datacleaning.domain;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "consumer_log")
@Data
public class ConsumerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unionid;

    private Long cbjId;

    private Long chjId;

    private String cbjAccount;

    private String chjAccount;

    /**
     * 状态 0: 失败 1:成功
     */
    private int status;
}
