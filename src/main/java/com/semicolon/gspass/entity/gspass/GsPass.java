package com.semicolon.gspass.entity.gspass;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolon.gspass.entity.grade.Grade;
import com.semicolon.gspass.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gspass")
public class GsPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "grade_id", referencedColumnName = "id"),
            @JoinColumn(name = "school_id", referencedColumnName = "school_id")
    })
    @JsonBackReference
    private Grade grade;

    @Column(name = "is_used", columnDefinition = "TINYINT(1)")
    private boolean used;

}
