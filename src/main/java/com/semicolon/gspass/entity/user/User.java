package com.semicolon.gspass.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.gspass.entity.gspass.GsPass;
import com.semicolon.gspass.entity.school.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {

    @Id
    @Column(length = 45, unique = true)
    private String id;

    @Column(length = 255)
    private String password;

    @Column(length = 5)
    private String gcn;

    @Column(length = 4)
    private String entryYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @JsonBackReference
    private School school;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<GsPass> gsPass;

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

}
