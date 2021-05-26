package com.semicolon.gspass.service.school;

import com.semicolon.gspass.dto.school.MealResponse;

public interface SchoolService {
    MealResponse getMeals(int schoolId, String date);
}
