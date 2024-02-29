package com.mybankingapp.exceptions;

public class InvalidAccountNumberException extends Exception{

    public InvalidAccountNumberException(){
        super();
    }

    public InvalidAccountNumberException(String errorMsg){
        super(errorMsg);
    }
}
