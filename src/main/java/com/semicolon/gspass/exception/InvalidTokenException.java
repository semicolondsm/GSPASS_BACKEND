package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class InvalidTokenException extends GsException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
