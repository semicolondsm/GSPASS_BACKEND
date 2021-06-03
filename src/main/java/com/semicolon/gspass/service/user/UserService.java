package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.user.UserRegisterRequest;
import com.semicolon.gspass.dto.TokenResponse;

public interface UserService {
    boolean nameIsExist(String name);
    TokenResponse register(UserRegisterRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse tokenRefresh(String token);
    void changePassword(PasswordRequest request);
    void applyGsPass();
}
