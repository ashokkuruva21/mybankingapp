package com.mybankingapp.controller;

import com.mybankingapp.entity.Transaction;
import com.mybankingapp.exceptions.ErrorMessage;
import com.mybankingapp.exceptions.InvalidAccountNumberException;
import com.mybankingapp.exceptions.InvalidAccountStateException;
import com.mybankingapp.service.AccountService;
import com.mybankingapp.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mybankingapp/account/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Operation(summary = "Fetch all the Transaction Details of an MyBankingApp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Fetched all the transaction details successfully of the MyBankingApp Account"),
            @ApiResponse(responseCode = "404", description = "Account number is Invalid", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Account is in Closed state", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long accountNumber) throws InvalidAccountNumberException, InvalidAccountStateException {
        List<Transaction> transactionDetails=transactionService.getTransactionDetails(accountNumber);
        return new ResponseEntity<>(transactionDetails, HttpStatus.OK);
    }

    @Operation(summary = "Fetch transaction details based on Transaction Number")
    @ApiResponse(responseCode = "200",description = "Transaction details are fetched successfully based on Transaction Number")
    @GetMapping("/transactions/transactionnumber/{transactionNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByTransactionNumber(@PathVariable Long transactionNumber){
        List<Transaction> transactionsByTransactionNumber = transactionService.getTransactionsByTransactionNumber(transactionNumber);
        return new ResponseEntity<>(transactionsByTransactionNumber,HttpStatus.OK);
    }
}
