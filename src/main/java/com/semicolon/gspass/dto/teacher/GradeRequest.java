package com.semicolon.gspass.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GradeRequest {

    private int id;
    private Time breakfast; //아침 먹는 시간
    private Time lunch;
    private Time dinner;

}
