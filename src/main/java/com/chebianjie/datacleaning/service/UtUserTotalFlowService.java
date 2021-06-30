package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtUserTotalFlow;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
public interface UtUserTotalFlowService {

    List<UtUserTotalFlow> cbjFindAllByUid(Long cbjId);

    List<UtUserTotalFlow> chjFindAllByUid(Long cbjId);


}
