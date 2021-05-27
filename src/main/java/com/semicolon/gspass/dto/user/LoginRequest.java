package com.semicolon.gspass.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String id;
    private String password;

    public UsernamePasswordAuthenticationToken getAuthToken() {
        return new UsernamePasswordAuthenticationToken(id, password);
    }

}
