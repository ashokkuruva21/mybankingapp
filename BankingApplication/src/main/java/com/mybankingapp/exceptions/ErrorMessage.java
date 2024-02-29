package com.mybankingapp.exceptions;

import lombok.Data;

@Data

public class ErrorMessage {

    private int errorCode;

    private String errorMsg;

}
