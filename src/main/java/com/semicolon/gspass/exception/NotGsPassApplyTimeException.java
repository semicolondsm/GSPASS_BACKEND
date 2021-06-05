package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class NotGsPassApplyTimeException extends GsException {
    public NotGsPassApplyTimeException() {
        super(ErrorCode.NOT_GSPASS_APPLY_TIME);
    }
}
