package com.semicolon.gspass.entity.school;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class School {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false)
    @NonNull
    private String schoolCode;

    @Column(length = 45, nullable = false)
    @NonNull
    private String scCode;

    @Column(length = 45, nullable = false, unique = true)
    private String randomCode;

    private Time breakfastPeriod;

    private Time lunchPeriod;

    private Time dinnerPeriod;

    @NonNull
    private int timeLength;

}
