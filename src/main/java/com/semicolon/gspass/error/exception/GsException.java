package com.semicolon.gspass.error.exception;

import lombok.Getter;

@Getter
public class GsException extends RuntimeException {

    private final ErrorCode errorCode;

    public GsException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
