package com.semicolon.gspass.service.teacher;

import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.RegisterRequest;

public interface TeacherService {
    TokenResponse registerTeacher(RegisterRequest request);
}