package com.semicolon.gspass.service.teacher;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.dto.teacher.GradeRequest;
import com.semicolon.gspass.dto.teacher.PassTimeRequest;
import com.semicolon.gspass.dto.teacher.RegisterRequest;
import com.semicolon.gspass.dto.teacher.SchoolInformationResponse;

public interface TeacherService {
    TokenResponse registerTeacher(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    void changePassword(PasswordRequest request);
    void setTime(GradeRequest request);
    void setPassTime(PassTimeRequest request);
    SchoolInformationResponse getInfo();
}
