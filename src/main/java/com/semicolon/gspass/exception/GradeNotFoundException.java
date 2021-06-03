package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class GradeNotFoundException extends GsException {
    public GradeNotFoundException() {
        super(ErrorCode.GRADE_NOT_FOUND);
    }
}
