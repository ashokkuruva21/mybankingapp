package com.mybankingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String generalExceptionHandler(Exception exception){
        return exception.getMessage();
    }

    @ExceptionHandler(InvalidAccountNumberException.class)
    public ResponseEntity<ErrorMessage> invalidAccountNumberException(InvalidAccountNumberException exception){
        ErrorMessage error=new ErrorMessage();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        error.setErrorMsg(exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorMessage> insufficientBalanceException(InsufficientBalanceException exception){
        ErrorMessage error=new ErrorMessage();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setErrorMsg(exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountStateException.class)
    public ResponseEntity<ErrorMessage> invalidAccountStateException(InvalidAccountStateException exception){
        ErrorMessage error=new ErrorMessage();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setErrorMsg(exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
