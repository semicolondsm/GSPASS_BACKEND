package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.dto.school.SchoolResponse;
import com.semicolon.gspass.service.school.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/meals")
    public MealResponse getMeals(@RequestParam("school_id") String schoolId, @RequestParam("date") String date) {
        return schoolService.getMeals(Integer.parseInt(schoolId), date);
    }

    @GetMapping("/school")
    public List<SchoolResponse> getSchools(@RequestParam("name") String name) {
        return schoolService.getSchools(name);
    }

}
