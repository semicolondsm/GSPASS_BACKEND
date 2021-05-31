package com.semicolon.gspass.exception;

import com.semicolon.gspass.error.exception.ErrorCode;
import com.semicolon.gspass.error.exception.GsException;

public class TeacherAlreadyExistException extends GsException {

    public TeacherAlreadyExistException() {
        super(ErrorCode.TEACHER_ALREADY_EXIST);
    }

}
