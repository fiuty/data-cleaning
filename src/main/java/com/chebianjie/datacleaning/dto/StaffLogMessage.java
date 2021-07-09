package com.chebianjie.datacleaning.dto;

import com.chebianjie.datacleaning.domain.enums.Platform;
import lombok.Data;

/**
 * @author zhengdayue
 * @date: 2021-07-09
 */
@Data
public class StaffLogMessage {

    private Long id;

    private Platform platform;

    private Long consumerId;
}
