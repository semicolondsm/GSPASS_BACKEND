package com.semicolon.gspass.entity.school;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.gspass.entity.teacher.Teacher;
import com.semicolon.gspass.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolCode","scCode"}))
@Entity(name = "school")
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

    private int timeLength;

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<User> students = new HashSet<>();

    @OneToOne(mappedBy = "school", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Teacher teacher;

}
