package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class SchoolNotFoundException extends GsException {

    public SchoolNotFoundException(){
        super(ErrorCode.SCHOOL_NOT_FOUND);
    }

}
