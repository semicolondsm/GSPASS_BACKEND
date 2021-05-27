package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.user.LoginRequest;
import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.user.TokenResponse;
import com.semicolon.gspass.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/overlap")
    public boolean checkName(@RequestParam("name") String name) {
        return userService.nameIsExist(name);
    }

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
