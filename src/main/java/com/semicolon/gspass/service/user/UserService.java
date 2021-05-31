package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.TokenResponse;

public interface UserService {
    boolean nameIsExist(String name);
    TokenResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse tokenRefresh(String token);
}
