package com.semicolon.gspass.dto.school;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SchoolRegisterRequest {

    @NotEmpty
    private String schoolCode;
    @NotEmpty
    private String scCode;
    @NotEmpty
    private String schoolName;

}
