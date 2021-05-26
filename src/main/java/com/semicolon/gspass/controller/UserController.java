package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.user.TokenResponse;
import com.semicolon.gspass.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

}
