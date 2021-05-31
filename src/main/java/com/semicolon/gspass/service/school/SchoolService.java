package com.semicolon.gspass.service.school;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.dto.school.RegisterRequest;
import com.semicolon.gspass.dto.school.SchoolResponse;

import java.util.List;

public interface SchoolService {
    MealResponse getMeals(String date);
    List<SchoolResponse> getSchools(String name);
    String registerSchool(RegisterRequest request);
}
