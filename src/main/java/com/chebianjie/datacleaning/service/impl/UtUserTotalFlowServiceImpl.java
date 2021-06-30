package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.UtUserTotalFlow;
import com.chebianjie.datacleaning.repository.UtUserTotalFlowRepository;
import com.chebianjie.datacleaning.service.UtUserTotalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UtUserTotalFlowServiceImpl implements UtUserTotalFlowService {

    @Autowired
    private UtUserTotalFlowRepository utUserTotalFlowRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public List<UtUserTotalFlow> cbjFindAllByUid(Long cbjId) {
        return utUserTotalFlowRepository.findAllByUid(cbjId);
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<UtUserTotalFlow> chjFindAllByUid(Long chjId) {
        return utUserTotalFlowRepository.findAllByUid(chjId);
    }
}
