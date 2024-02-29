package com.mybankingapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "unique_transaction_id_sequence",
            sequenceName = "unique_transaction_id_sequence",
            initialValue = 500000001,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "unique_transaction_id_sequence"
    )
    private Long uniqueTransactionId;

//    @SequenceGenerator(
//            name = "transaction_number_sequence",
//            sequenceName = "transaction_number_sequence",
//            initialValue = 300000001,
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "transaction_number_sequence"
//    )
    private Long transactionNumber;

    private String transactionType;

//    @ManyToOne
//    @JoinColumn(name = "account_number")
//    private Account account;

    private Long accountNumber;

    private LocalDateTime localDateTime;

    private double amount;

    private double balance;

    public Transaction(LocalDateTime localDateTime,String transactionType,double amount,double balance,Long accountNumber){
        this.localDateTime=localDateTime;
        this.transactionType=transactionType;
        this.amount=amount;
        this.balance=balance;
        this.accountNumber=accountNumber;
    }
}
