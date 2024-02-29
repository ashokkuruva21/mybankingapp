package com.mybankingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class Account {

    @Id
    @SequenceGenerator(
            name = "account_number_sequence",
            sequenceName = "account_number_sequence",
            initialValue = 1000001,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_number_sequence"
    )
    private Long accountNumber;

    @NotBlank(message = "{name.not.blank}")
    private String name;

    @Min(value = 0, message = "{age.invalid}")
    private int age;

    @NotNull(message = "{mobile.number.empty}")
    private Long mobileNumber;

    @Min(value = 0, message = "{deposit.invalid}")
    private double balance;

    @NotNull(message = "{account.status.null}")
    @Pattern(regexp = "^(Active|Closed)$",message = "{account.status.invalid}")
    private String accountStatus;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "account_transaction",
    joinColumns = @JoinColumn(name = "account_number"),
    inverseJoinColumns = @JoinColumn(name = "transaction_number"))
    private List<Transaction> transactions=new ArrayList<>();

    public Account(String name,int age, Long mobileNumber, double balance,String accountStatus){
        this.name=name;
        this.age=age;
        this.mobileNumber=mobileNumber;
        this.balance=balance;
        this.accountStatus=accountStatus;
    }
}
