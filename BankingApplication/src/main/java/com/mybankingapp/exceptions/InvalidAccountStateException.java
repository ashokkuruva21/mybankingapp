package com.mybankingapp.exceptions;

public class InvalidAccountStateException extends Exception{

    public InvalidAccountStateException(){
        super();
    }

    public InvalidAccountStateException(String errorMsg){
        super(errorMsg);
    }
}
