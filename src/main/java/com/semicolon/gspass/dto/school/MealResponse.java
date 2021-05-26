package com.semicolon.gspass.dto.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MealResponse {

    private List<String> breakfast;
    private List<String> lunch;
    private List<String> dinner;

}
