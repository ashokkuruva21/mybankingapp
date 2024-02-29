package com.mybankingapp.service;

import com.mybankingapp.entity.Account;
import com.mybankingapp.entity.Transaction;
import com.mybankingapp.exceptions.InsufficientBalanceException;
import com.mybankingapp.exceptions.InvalidAccountNumberException;
import com.mybankingapp.exceptions.InvalidAccountStateException;
import com.mybankingapp.repository.AccountRepository;
import com.mybankingapp.repository.TransactionRepository;
import com.mybankingapp.util.BankConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class AccountServiceImpl implements AccountService{

    @Autowired
    Environment environment;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public String createAccount(Account account) {
        Account createdAccount = accountRepository.save(account);
        Transaction transaction=new Transaction(LocalDateTime.now(),"Initial",account.getBalance(),createdAccount.getBalance(),createdAccount.getAccountNumber());
        Transaction savedTransaction = transactionRepository.save(transaction);
        savedTransaction.setTransactionNumber(savedTransaction.getUniqueTransactionId()-200000000);
        createdAccount.getTransactions().add(0,savedTransaction);
        createdAccount.setTransactions(createdAccount.getTransactions());
        accountRepository.save(createdAccount);

        return "Account is Created Successfully with following details \n"+account;
    }

    @Override
    public String closeAccount(Long accountNumber) throws InvalidAccountNumberException,InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);

        if(optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        else{
            Account account=optionalAccount.get();
            if(account.getAccountStatus().equals("Closed")){
                throw new InvalidAccountStateException("Account is already in Closed!");
            }
            else{
                Transaction transaction=new Transaction(LocalDateTime.now(),"Closed",account.getBalance(),0,accountNumber);
                Transaction savedTransaction = transactionRepository.save(transaction);
                savedTransaction.setTransactionNumber(savedTransaction.getUniqueTransactionId()-200000000);
                account.getTransactions().add(0,savedTransaction);
                account.setBalance(0);
                account.setTransactions(account.getTransactions());
                account.setAccountStatus("Closed");
                accountRepository.save(account);

                return "Account with account number "+accountNumber+" is closed successfully";
            }
        }
    }

    @Override
    public String deleteAccount(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);

        if(optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        else{

            if(optionalAccount.get().getAccountStatus().equals("Closed")){
                accountRepository.deleteById(accountNumber);
            }
            else{
                this.closeAccount(accountNumber);
                accountRepository.deleteById(accountNumber);
            }
            return "Account with account number "+accountNumber+" is deleted successfully";
        }

    }

    @Override
    public String depositAmount(Long accountNumber, double amount) throws InvalidAccountNumberException, InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);

        if(optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        else{
            if(optionalAccount.get().getAccountStatus().equals("Closed")){
                throw new InvalidAccountStateException("Account is in Closed State! Please ReActivate Account and try again!");
            }

            accountRepository.depositAmount(accountNumber,amount);

            Account account = optionalAccount.get();
            account.setBalance(account.getBalance()+amount);
            Account accountAfterDeposited = accountRepository.save(account);
            double balance=accountAfterDeposited.getBalance();
            Transaction transaction=new Transaction(LocalDateTime.now(),"Deposit",amount,balance,accountNumber);
            Transaction savedTransaction = transactionRepository.save(transaction);
            savedTransaction.setTransactionNumber(savedTransaction.getUniqueTransactionId()-200000000);
            account.getTransactions().add(0,savedTransaction);
            account.setTransactions(account.getTransactions());
            accountRepository.save(account);

            return amount+" is deposited successfully to the account number "+accountNumber;
        }
    }

    @Override
    public String withdrawAmount(Long accountNumber, double withdrawAmount) throws InvalidAccountNumberException, InsufficientBalanceException, InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);

        if(optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        else{
            if(optionalAccount.get().getAccountStatus().equals("Closed")){
                throw new InvalidAccountStateException("Account is in Closed State! Please ReActivate Account and try again!");
            }
            Account account=optionalAccount.get();
            double balance=account.getBalance();
            if(balance>=withdrawAmount){
                accountRepository.withdrawAmount(accountNumber,withdrawAmount);

                Account accountForBalanceCheck=optionalAccount.get();
                accountForBalanceCheck.setBalance(accountForBalanceCheck.getBalance()-withdrawAmount);
                Account accountAfterWithdraw=accountRepository.save(accountForBalanceCheck);
                double balanceAfterWithdraw=accountAfterWithdraw.getBalance();

                Transaction transaction=new Transaction(LocalDateTime.now(),"Withdraw",withdrawAmount,balanceAfterWithdraw,accountNumber);
                Transaction savedTransaction=transactionRepository.save(transaction);
                savedTransaction.setTransactionNumber(savedTransaction.getUniqueTransactionId()-200000000);
                accountForBalanceCheck.getTransactions().add(0,savedTransaction);
                accountForBalanceCheck.setTransactions(accountForBalanceCheck.getTransactions());
                accountRepository.save(accountForBalanceCheck);

                return withdrawAmount+" is withdrawn successfully from account number "+accountNumber;

            }
            else {
                throw new InsufficientBalanceException(environment.getProperty(BankConstants.INSUFFICIENT_BALANCE.toString()));
            }

        }
    }

    @Override
    public String showBalance(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);

        if (optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        else{
            if(optionalAccount.get().getAccountStatus().equals("Closed")){
                throw new InvalidAccountStateException("Account is in Closed State! Please ReActivate Account and try again!");
            }
            Account account=optionalAccount.get();
            return "Available balance is "+account.getBalance();
        }
    }

    @Override
    public String fundTransfer(Long fromAccountNumber, Long toAccountNumber, double amount) throws InvalidAccountNumberException, InsufficientBalanceException, InvalidAccountStateException {
        Optional<Account> fromOptionalAccount = accountRepository.findById(fromAccountNumber);
        if(fromOptionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_SENDER_ACCOUNT_NUMBER.toString()));
        }
        if(fromOptionalAccount.get().getAccountStatus().equals("Closed")){
                throw new InvalidAccountStateException("Sender Account is in Closed State!");
        }
        else{
            Optional<Account> toOptionalAccount = accountRepository.findById(toAccountNumber);
            if(toOptionalAccount.isEmpty()){
                throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_RECEIVER_ACCOUNT_NUMBER.toString()));
            }
            if(toOptionalAccount.get().getAccountStatus().equals("Closed")){
                throw new InvalidAccountStateException("Receiver Account is in Closed State!");
            }
            else{
                Account fromAccount=fromOptionalAccount.get();
                if(fromAccount.getBalance()<amount){
                    throw new InsufficientBalanceException(environment.getProperty(BankConstants.INSUFFICIENT_BALANCE.toString()));
                }
                else{
                    this.withdrawAmount(fromAccountNumber,amount);

                    Transaction senderTransactionAfterTransfer=fromAccount.getTransactions().get(0);
                    senderTransactionAfterTransfer.setTransactionNumber(senderTransactionAfterTransfer.getUniqueTransactionId()-200000000);
                    senderTransactionAfterTransfer.setTransactionType("Transferred Fund");
                    transactionRepository.save(senderTransactionAfterTransfer);

                    this.depositAmount(toAccountNumber,amount);
                    Transaction receiverTransactionAfterTransfer=toOptionalAccount.get().getTransactions().get(0);
                    receiverTransactionAfterTransfer.setTransactionType("Fund Received");
                    receiverTransactionAfterTransfer.setTransactionNumber(senderTransactionAfterTransfer.getTransactionNumber());
                    transactionRepository.save(receiverTransactionAfterTransfer);

                    return amount+" from "+fromAccountNumber+" transferred successfully to "+toAccountNumber;
                }
            }
        }
    }

    @Override
    public List<Account> getAllAccounts() {
       return accountRepository.findAll();
    }

    @Override
    public String reactivateAccount(Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
        if(optionalAccount.isEmpty()){
            throw new InvalidAccountNumberException(environment.getProperty(BankConstants.INVALID_ACCOUNT_NUMBER.toString()));
        }
        else{
            Account account=optionalAccount.get();
            if(account.getAccountStatus().equals("Active")){
                throw new InvalidAccountStateException("Account is already in Active State");
            }
            else{
                account.setAccountStatus("Active");
                accountRepository.save(account);
                return "Account with Account Number "+accountNumber+" is ReActivated successfully";
            }
        }
    }


}
