package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class AlreadyUserExistException extends GsException {
    public AlreadyUserExistException() {
        super(ErrorCode.ALREADY_USER_EXIST);
    }
}
