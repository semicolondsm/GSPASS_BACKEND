package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class GsPassAlreadyUseException extends GsException {

    public GsPassAlreadyUseException() {
        super(ErrorCode.GSPASS_ALREADY_USE);
    }

}
