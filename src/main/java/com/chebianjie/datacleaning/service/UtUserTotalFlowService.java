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

    int cbjCountByCreateTimeBetween(Long timeFrom, Long timeTo);

    int chjCountByCreateTimeBetween(Long timeFrom, Long timeTo);

    List<UtUserTotalFlow> cbjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize);

    List<UtUserTotalFlow> chjFindAllByCreateTimeBetween(Long timeFrom, Long timeTo, int pageNumber, int pageSize);

    /**
     * 流水增量
     */
    void utUserTotalFlowJob();

    UtUserTotalFlow cbjFindById(Long id);

    UtUserTotalFlow chjFindById(Long id);
}
