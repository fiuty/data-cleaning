package com.chebianjie.datacleaning.repository;




import com.chebianjie.datacleaning.domain.ConsumerBalance;
import com.chebianjie.datacleaning.domain.enums.BalanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhengdayue
 * @date: 2021-06-08
 */
public interface ConsumerBalanceRepository extends JpaRepository<ConsumerBalance, Long>, JpaSpecificationExecutor<ConsumerBalance> {

    List<ConsumerBalance> findByUnionAccount(String unionAccount);

    ConsumerBalance findOneByConsumerIdAndBalanceType(long consumerId, BalanceType balanceType);

    List<ConsumerBalance> findAllByUnionAccountIn(List<String> unionAccounts);

}
