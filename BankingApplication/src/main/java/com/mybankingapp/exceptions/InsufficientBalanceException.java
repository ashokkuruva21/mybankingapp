package com.mybankingapp.exceptions;

public class InsufficientBalanceException extends Exception{


    public InsufficientBalanceException(){
        super();
    }
    public InsufficientBalanceException(String errorMsg){
        super(errorMsg);
    }
}
