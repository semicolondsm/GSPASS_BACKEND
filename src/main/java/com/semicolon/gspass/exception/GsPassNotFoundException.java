package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class GsPassNotFoundException extends GsException {

    public GsPassNotFoundException() {
        super(ErrorCode.GSPASS_NOT_FOUND);
    }

}
