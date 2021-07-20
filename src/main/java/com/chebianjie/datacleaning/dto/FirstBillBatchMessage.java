package com.chebianjie.datacleaning.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-07-20
 */
@Data
public class FirstBillBatchMessage {

    /**
     * 用户id集
     */
    private List<Long> ids;
}
