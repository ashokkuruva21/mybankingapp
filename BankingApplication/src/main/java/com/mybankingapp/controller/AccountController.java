package com.mybankingapp.controller;

import com.mybankingapp.entity.Account;
import com.mybankingapp.entity.Transaction;
import com.mybankingapp.exceptions.ErrorMessage;
import com.mybankingapp.exceptions.InsufficientBalanceException;
import com.mybankingapp.exceptions.InvalidAccountNumberException;
import com.mybankingapp.exceptions.InvalidAccountStateException;
import com.mybankingapp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mybankingapp/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Operation(summary = "Create an Account in MyBankingApp")
    @ApiResponse(responseCode = "201",description = "Account is successfully created in MyBankingApp")
    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody Account account){
        String createdAccount= accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
    @Operation(summary = "Deleting an Account from MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account is deleted successfully from MyBankingApp"),
                           @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountNumber") Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        String deletedAccount = accountService.deleteAccount(accountNumber);
        return ResponseEntity.status(HttpStatus.OK).body(deletedAccount);
    }

    @Operation(summary = "Close an Account from MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account is closed successfully from MyBankingApp",content = @Content),
                           @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
                           @ApiResponse(responseCode = "400",description = "Account is already Closed",content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @PutMapping("/close/{accountNumber}")
    public ResponseEntity<String> closeAccount(@PathVariable("accountNumber") Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        String closedAccount = accountService.closeAccount(accountNumber);
        return new ResponseEntity<>(closedAccount,HttpStatus.OK);
    }

    @Operation(summary = "Deposit Amount into the MyBankingApp Account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Amount is successfully deposited to the MyBankingApp Account"),
                           @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
                           @ApiResponse(responseCode = "400", description = "Account is in Closed state", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @PutMapping("/deposit/{accountNumber}/{amount}")
    public ResponseEntity<String> depositAmount(@PathVariable("accountNumber") Long accountNumber,@PathVariable("amount") double amount) throws InvalidAccountNumberException, InvalidAccountStateException {
        String depositAmount=accountService.depositAmount(accountNumber,amount);
        return new ResponseEntity<>(depositAmount,HttpStatus.OK);
    }

    @Operation(summary = "Withdraw Amount from the Account of MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Amount is withdrawn successfully from the MyBankingApp Account"),
                           @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
                           @ApiResponse(responseCode = "400", description = "Insufficient Balance in the Account or Account is in Closed State", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @PutMapping("/withdraw/{accountNumber}/{amount}")
    public ResponseEntity<String> withdrawAmount(@PathVariable("accountNumber") Long accountNumber,@PathVariable("amount") double amount) throws InvalidAccountNumberException, InsufficientBalanceException, InvalidAccountStateException {
        String withdrawAmount=accountService.withdrawAmount(accountNumber,amount);
        return new ResponseEntity<>(withdrawAmount,HttpStatus.OK);
    }

    @Operation(summary = "Fetching all accounts in MyBankingApp")
    @ApiResponse(responseCode = "200",description = "All Accounts of the MyBankingApp are fetched successfully")
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> allAccounts=accountService.getAllAccounts();
        return new ResponseEntity<>(allAccounts,HttpStatus.OK);
    }

    @Operation(summary = "Fetching the balance of an Account in MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Fetched the balance successfully from the MyBankingApp Account"),
                           @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
                           @ApiResponse(responseCode = "400", description = "Account is in Closed state", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/getbalance/{accountNumber}")
    public ResponseEntity<String> getBalance(@PathVariable Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        String showBalance = accountService.showBalance(accountNumber);
        return new ResponseEntity<>(showBalance,HttpStatus.OK);
    }

    @Operation(summary = "Transfer of Funds from One Account to Another Account in MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Fund/Amount is transferred successfully"),
            @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Insufficient Balance in the Account or Sender Account/Receiver Account is in closed state", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @PutMapping("/fundtransfer/{fromAccountNumber}/{toAccountNumber}/{amount}")
    public ResponseEntity<String> fundTransfer(@PathVariable Long fromAccountNumber,@PathVariable Long toAccountNumber,@PathVariable double amount) throws InvalidAccountNumberException, InsufficientBalanceException, InvalidAccountStateException {
        String fundTransfer= accountService.fundTransfer(fromAccountNumber,toAccountNumber,amount);
        return new ResponseEntity<>(fundTransfer,HttpStatus.OK);
    }


    @Operation(summary = "Reactivate the Closed Account in MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Account is Reactivated Successfully"),
                           @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
                           @ApiResponse(responseCode = "400", description = "Account is already in Active state", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @PutMapping("/reactivate/{accountNumber}")
    public ResponseEntity<String> reactiveAccount(@PathVariable Long accountNumber) throws InvalidAccountStateException, InvalidAccountNumberException {
        String reactivatedAccount = accountService.reactivateAccount(accountNumber);
        return new ResponseEntity<>(reactivatedAccount,HttpStatus.OK);
    }

}
