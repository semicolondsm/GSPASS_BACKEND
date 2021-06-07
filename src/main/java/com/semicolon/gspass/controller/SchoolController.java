package com.semicolon.gspass.controller;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.dto.school.SchoolRegisterRequest;
import com.semicolon.gspass.dto.school.SchoolResponse;
import com.semicolon.gspass.service.school.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT", name = "Authorization", in = SecuritySchemeIn.HEADER)
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/meals")
    @Operation(summary = "급식", security = @SecurityRequirement(name = "Authorization"))
    public MealResponse getMeals(@RequestParam("day") int day) {
        return schoolService.getMeals(day);
    }

    @GetMapping("/school")
    @Operation(summary = "학교 정보")
    public List<SchoolResponse> getSchools(@RequestParam("name") String name) {
        return schoolService.getSchools(name);
    }

    @PostMapping("/school")
    @Operation(summary = "학교 등록")
    public String registerSchool(@Valid @RequestBody SchoolRegisterRequest request) {
        return schoolService.registerSchool(request);
    }

}
