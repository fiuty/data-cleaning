package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.StaffInviteDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffInviteDetailService {


    public Page<StaffInviteDetail> getCbjAllStaffInviteDetail(Pageable pageable);

    public void updateCbjStaffInviteDetailById(String consumerUnionAccount,Long id);

    public Page<StaffInviteDetail> getChjAllStaffInviteDetail(Pageable pageable);

    public void updateChjStaffInviteDetailById(String consumerUnionAccount,Long id);





}
