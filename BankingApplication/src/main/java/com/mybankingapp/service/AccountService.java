package com.mybankingapp.service;

import com.mybankingapp.entity.Account;
import com.mybankingapp.entity.Transaction;
import com.mybankingapp.exceptions.InsufficientBalanceException;
import com.mybankingapp.exceptions.InvalidAccountNumberException;
import com.mybankingapp.exceptions.InvalidAccountStateException;

import java.util.List;


public interface AccountService {

    public String createAccount(Account account);

    public String closeAccount(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException;

    public String deleteAccount(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException;

    public String depositAmount(Long accountNumber, double amount) throws InvalidAccountNumberException, InvalidAccountStateException;

    public String withdrawAmount(Long accountNumber,double withdrawAmount) throws InvalidAccountNumberException, InsufficientBalanceException, InvalidAccountStateException;

    public String showBalance(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException;

    public String fundTransfer(Long fromAccountNumber,Long toAccountNumber,double amount) throws InvalidAccountNumberException, InsufficientBalanceException, InvalidAccountStateException;

    public List<Account> getAllAccounts();

    public String reactivateAccount(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException;


}
