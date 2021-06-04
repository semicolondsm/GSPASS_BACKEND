package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class GsPassAlreadyApplyException extends GsException {

    public GsPassAlreadyApplyException() {
        super(ErrorCode.GSPASS_ALREADY_APPLY);
    }

}
