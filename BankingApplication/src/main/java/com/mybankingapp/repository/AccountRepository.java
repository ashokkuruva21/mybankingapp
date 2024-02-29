package com.mybankingapp.repository;

import com.mybankingapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Transactional
    @Modifying
    @Query(value = "update account set balance=balance+?2 where account_number=?1",nativeQuery = true)
    public void depositAmount(Long accountNumber,double amount);

    @Transactional
    @Modifying
    @Query(value = "update account set balance=balance-?2 where account_number=?1",nativeQuery = true)
    public void withdrawAmount(Long accountNumber,double withdrawAmount);

}
