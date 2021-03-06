package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.user.GsPassResponse;
import com.semicolon.gspass.dto.user.GsPassTimeResponse;
import com.semicolon.gspass.dto.user.UserInformationResponse;
import com.semicolon.gspass.dto.user.UserRegisterRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public TokenResponse register(@Valid @RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    public TokenResponse tokenRefresh(@RequestHeader(name = "X-Refresh-Token") String token) {
        return userService.tokenRefresh(token);
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "비밀번호 변경", security = @SecurityRequirement(name = "Authorization"))
    public void changePassword(@Valid @RequestBody PasswordRequest request) {
        userService.changePassword(request);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "급식패스 신청", security = @SecurityRequirement(name = "Authorization"))
    public GsPassResponse applyGsPass() {
        return userService.applyGsPass();
    }

    @GetMapping("/information")
    @Operation(summary = "유저 정보", security = @SecurityRequirement(name = "Authorization"))
    public UserInformationResponse getUserInfo() {
        return userService.getUserInfo();
    }
    
    @GetMapping("/gspass")
    @Operation(summary = "급식 패스 정보", security = @SecurityRequirement(name = "Authorization"))
    public GsPassResponse getPassInfo() {
        return userService.getPassInfo();
    }

    @GetMapping("/gspass/time")
    @Operation(summary = "다음 급식 패스 시간", security = @SecurityRequirement(name = "Authorization"))
    public GsPassTimeResponse getNextGsPassTime() {
        return userService.getNextGsPassTime();
    }

    @PostMapping("/gspass")
    @Operation(summary = "급식패스 사용", security = @SecurityRequirement(name = "Authorization"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void useGsPass() {
        userService.useGsPass();
    }

}
