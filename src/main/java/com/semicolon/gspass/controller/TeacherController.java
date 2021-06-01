package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.RegisterRequest;
import com.semicolon.gspass.service.teacher.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    @Operation(summary = "관리자 회원가입")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return teacherService.registerTeacher(request);
    }

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return teacherService.login(request);
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "관리자 비밀번호 변경", security = @SecurityRequirement(name = "Authorization"))
    public void changePassword(@RequestBody PasswordRequest request) {
        teacherService.changePassword(request);
    }

}
