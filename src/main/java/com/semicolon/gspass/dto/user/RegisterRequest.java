package com.semicolon.gspass.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String id;
    private String name;
    private String password;
    private String gcn;
    private String entryYear;
    private String randomCode;

}
