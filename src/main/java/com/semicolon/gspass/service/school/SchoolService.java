package com.semicolon.gspass.service.school;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.dto.school.SchoolRegisterRequest;
import com.semicolon.gspass.dto.school.SchoolResponse;
import com.semicolon.gspass.dto.teacher.SchoolInformationResponse;

import java.util.List;

public interface SchoolService {
    MealResponse getMeals(int day);
    List<SchoolResponse> getSchools(String name);
    SchoolInformationResponse registerSchool(SchoolRegisterRequest request);
}
