package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/overlap")
    @Operation(summary = "ID 중복 확인")
    public boolean checkName(@RequestParam("name") String name) {
        return userService.nameIsExist(name);
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    public TokenResponse tokenRefresh(@RequestHeader(name = "X-Refresh-Token") String token) {
        return userService.tokenRefresh(token);
    }

}
