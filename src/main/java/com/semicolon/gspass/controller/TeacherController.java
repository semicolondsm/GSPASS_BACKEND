package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.RegisterRequest;
import com.semicolon.gspass.service.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/teacher/register")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return teacherService.registerTeacher(request);
    }

}
