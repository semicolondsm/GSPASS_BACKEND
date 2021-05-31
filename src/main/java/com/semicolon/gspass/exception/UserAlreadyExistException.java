package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class UserAlreadyExistException extends GsException {
    public UserAlreadyExistException() {
        super(ErrorCode.USER_ALREADY_EXIST);
    }
}
