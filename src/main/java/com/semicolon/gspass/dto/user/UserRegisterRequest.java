package com.semicolon.gspass.dto.user;

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
public class UserRegisterRequest {

    @NotEmpty
    private String id;
    @NotEmpty
    private String password;
    @NotEmpty
    private String gcn;
    @NotEmpty
    private String entryYear;
    @NotEmpty
    private String randomCode;

}
