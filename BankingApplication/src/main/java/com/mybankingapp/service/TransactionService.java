package com.mybankingapp.service;

import com.mybankingapp.entity.Transaction;
import com.mybankingapp.exceptions.InvalidAccountNumberException;
import com.mybankingapp.exceptions.InvalidAccountStateException;

import java.util.List;

public interface TransactionService {
    public List<Transaction> getTransactionDetails(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException;

    public List<Transaction> getTransactionsByTransactionNumber(Long transactionNumber);
}
