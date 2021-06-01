package com.semicolon.gspass.entity.teacher;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolon.gspass.entity.school.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Teacher {

    @Id @Column(unique = true)
    private String id;

    @Column(length = 255)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @JsonBackReference
    private School school;

    public Teacher setPassword(String password) {
        this.password = password;
        return this;
    }

}
