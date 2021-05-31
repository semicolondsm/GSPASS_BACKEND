package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class TeacherNotFoundException extends GsException {

    public TeacherNotFoundException() {
        super(ErrorCode.TEACHER_NOT_FOUND);
    }

}
