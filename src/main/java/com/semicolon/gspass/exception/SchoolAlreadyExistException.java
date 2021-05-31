package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class SchoolAlreadyExistException extends GsException {

    public SchoolAlreadyExistException() {
        super(ErrorCode.SCHOOL_ALREADY_EXIST);
    }

}
