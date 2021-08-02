package com.chebianjie.datacleaning.service;


import com.chebianjie.datacleaning.domain.UtFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UtFeedbackService {


    /**
     * 车便捷
     * @param pageable
     * @return
     */
    public Page<UtFeedback> getCbjAllUtFeedback(Pageable pageable);

    public void updateCbjUtFeedbackById(String consumerUnionAccount,Long id);

    public Page<UtFeedback> getCbjCouponAllUtFeedback(Pageable pageable);

    public void updateCbjCouponUtFeedbackById(String consumerUnionAccount,Long id);


    /**
     * 车惠捷
     * @param pageable
     * @return
     */
    public Page<UtFeedback> getChjAllUtFeedback(Pageable pageable);

    public void updateChjUtFeedbackById(String consumerUnionAccount,Long id);


    public Page<UtFeedback> getChjCouponAllUtFeedback(Pageable pageable);

    public void updateChjCouponUtFeedbackById(String consumerUnionAccount,Long id);







}
