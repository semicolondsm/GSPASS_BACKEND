package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/meals")
    public MealResponse getMeals(@RequestParam("school_id") String schoolId, @RequestParam("date") String date) {
        return schoolService.getMeals(Integer.parseInt(schoolId), date);
    }

}
