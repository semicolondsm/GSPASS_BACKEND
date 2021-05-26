package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class ParseErrorException extends GsException {

    public ParseErrorException() {
        super(ErrorCode.PARSE_ERROR);
    }

}
