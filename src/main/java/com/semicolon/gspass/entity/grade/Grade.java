package com.semicolon.gspass.entity.grade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolon.gspass.entity.school.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GradeId.class)
@Entity(name = "grade")
public class Grade implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @JsonBackReference
    private School school;

    @Id
    private Integer id;

    private Time breakfast;

    private Time lunch;

    private Time dinner;

}
