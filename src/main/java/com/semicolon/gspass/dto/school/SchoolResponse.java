package com.semicolon.gspass.dto.school;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchoolResponse {

    private String scCode;
    private String schoolCode;
    private String location;
    private String name;

}
