package com.mybankingapp.service;

import com.mybankingapp.entity.Account;
import com.mybankingapp.entity.Transaction;
import com.mybankingapp.exceptions.InvalidAccountNumberException;
import com.mybankingapp.exceptions.InvalidAccountStateException;
import com.mybankingapp.repository.AccountRepository;
import com.mybankingapp.repository.TransactionRepository;
import com.mybankingapp.util.BankConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    Environment environment;
    @Override
    public List<Transaction> getTransactionDetails(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);

        if(optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        if(optionalAccount.get().getAccountStatus().equals("Closed")){
            throw new InvalidAccountStateException(environment.getProperty(BankConstants.INVALID_ACCOUNT_STATE.toString()));
        }
        else{
            List<Transaction> transactions=new ArrayList<>();
            transactions.addAll(optionalAccount.get().getTransactions());
            List<Transaction> transactionListAfterSorting=transactions.stream().sorted(Comparator.comparingLong(Transaction::getUniqueTransactionId).reversed()).collect(Collectors.toList());
            return transactionListAfterSorting;
        }
    }

    public List<Transaction> getTransactionsByTransactionNumber(Long transactionNumber){
        return transactionRepository.getTransactionByTransactionNumber(transactionNumber);
    }
}
