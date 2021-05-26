package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.user.TokenResponse;

public interface UserService {
    TokenResponse register(RegisterRequest request);
}
