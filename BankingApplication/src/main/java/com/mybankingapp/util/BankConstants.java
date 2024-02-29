package com.mybankingapp.util;

public enum BankConstants {

    INVALID_ACCOUNT_NUMBER("invalid.account.number"),
    INVALID_SENDER_ACCOUNT_NUMBER("invalid.sender.account.number"),
    INVALID_RECEIVER_ACCOUNT_NUMBER("invalid.receiver.account.number"),
    INSUFFICIENT_BALANCE("insufficient.balance"),
    INVALID_ACCOUNT_STATE("invalid.account.state");

    private final String type;

    BankConstants(String type){
        this.type=type;
    }

    @Override
    public String toString(){
        return this.type;
    }

}


