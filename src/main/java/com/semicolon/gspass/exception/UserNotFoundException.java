package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class UserNotFoundException extends GsException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

}
