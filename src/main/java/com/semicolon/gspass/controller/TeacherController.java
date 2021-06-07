package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.GradeRequest;
import com.semicolon.gspass.dto.teacher.PassTimeRequest;
import com.semicolon.gspass.dto.teacher.RegisterRequest;
import com.semicolon.gspass.dto.teacher.SchoolInformationResponse;
import com.semicolon.gspass.service.teacher.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    @Operation(summary = "관리자 회원가입")
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        return teacherService.registerTeacher(request);
    }

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return teacherService.login(request);
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "관리자 비밀번호 변경", security = @SecurityRequirement(name = "Authorization"))
    public void changePassword(@Valid @RequestBody PasswordRequest request) {
        teacherService.changePassword(request);
    }

    @PostMapping("/grade/time")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "학년별 급식 시간 설정", security = @SecurityRequirement(name = "Authorization"))
    public void setTime(@Valid @RequestBody GradeRequest request) {
        teacherService.setTime(request);
    }

    @PostMapping("/school/time")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "급식패스 신청시간 설정", security = @SecurityRequirement(name = "Authorization"), description = "급식패스 신청시간 설정")
    public void setPassTime(@RequestBody PassTimeRequest request) {
        teacherService.setPassTime(request);
    }

    @GetMapping("/school/information")
    @Operation(summary = "학교 정보 가져오기", security = @SecurityRequirement(name = "Authorization"))
    public SchoolInformationResponse getInfo() {
        return teacherService.getInfo();
    }

}
