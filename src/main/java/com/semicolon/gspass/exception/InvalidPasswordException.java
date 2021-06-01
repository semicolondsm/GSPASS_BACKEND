package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class InvalidPasswordException extends GsException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }

}
