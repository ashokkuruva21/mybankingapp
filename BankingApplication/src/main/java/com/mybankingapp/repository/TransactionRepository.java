package com.mybankingapp.repository;

import com.mybankingapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query(value = "select * from Transaction where transaction_number=?1",nativeQuery = true)
    public List<Transaction> getTransactionByTransactionNumber(Long transactionNumber);
}
