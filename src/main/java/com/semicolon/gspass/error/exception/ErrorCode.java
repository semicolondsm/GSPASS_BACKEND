package com.semicolon.gspass.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    USER_NOT_FOUND(404, "User Not Found."),

    SCHOOL_NOT_FOUND(404, "School Not Found."),
    PARSE_ERROR(400, "Parse Error.");

    private final int status;
    private final String message;

}
