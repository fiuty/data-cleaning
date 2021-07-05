package com.chebianjie.datacleaning.dto;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

/**
 * @author zhengdayue
 * @date: 2021-07-05
 */
@Data
public class UtUserTotalFlowMessage {

    private Long id;

    private Platform platform;
}
