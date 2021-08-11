package com.chebianjie.datacleaning.dto;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

/**
 * @author zhengdayue
 * @date: 2021-08-05
 */
@Data
public class AddBillLogMessage {

    private Long utUserTotalFlowId;

    private Platform platform;
}
