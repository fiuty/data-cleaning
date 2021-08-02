package com.chebianjie.datacleaning.service;

import com.chebianjie.datacleaning.domain.UtReviewsReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UtReviewsReplyService {


    /**
     * 车便捷
     */
    public Page<UtReviewsReply> getCbjAllUtReviewsReply(Pageable pageable);

    public void updateCbjUtReviewsReplyById(String consumerUnionAccount,Long id);

    public Page<UtReviewsReply> getCbjCouponAllUtReviewsReply(Pageable pageable);

    public void updateCbjCouponUtReviewsReplyById(String consumerUnionAccount,Long id);








    /**
     * 车惠捷
     */
    public Page<UtReviewsReply> getChjAllUtReviewsReply(Pageable pageable);

    public void updateChjUtReviewsReplyById(String consumerUnionAccount,Long id);


    public Page<UtReviewsReply> getChjCouponAllUtReviewsReply(Pageable pageable);

    public void updateChjCouponUtReviewsReplyById(String consumerUnionAccount,Long id);


}
