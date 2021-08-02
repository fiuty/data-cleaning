package com.chebianjie.datacleaning.service.impl;

import com.chebianjie.datacleaning.common.annotation.DataSource;
import com.chebianjie.datacleaning.common.enums.DataSourcesType;
import com.chebianjie.datacleaning.domain.StaffInviteDetail;
import com.chebianjie.datacleaning.repository.StaffInviteDetailRepository;
import com.chebianjie.datacleaning.service.StaffInviteDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StaffInviteDetailServiceImpl implements StaffInviteDetailService {

    @Autowired
    StaffInviteDetailRepository staffInviteDetailRepository;



    @Override
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public Page<StaffInviteDetail> getCbjAllStaffInviteDetail(Pageable pageable) {
        return staffInviteDetailRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CBJ_STAFF)
    public void updateCbjStaffInviteDetailById(String consumerUnionAccount,Long id) {
        staffInviteDetailRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


    @Override
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public Page<StaffInviteDetail> getChjAllStaffInviteDetail(Pageable pageable) {
        return staffInviteDetailRepository.findAll(pageable);
    }

    @Override
    @DataSource(name = DataSourcesType.CHJ_STAFF)
    public void updateChjStaffInviteDetailById(String consumerUnionAccount, Long id) {
        staffInviteDetailRepository.updateConsumerUnionAccountById(consumerUnionAccount,id);
    }


}
