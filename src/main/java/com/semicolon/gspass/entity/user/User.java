package com.semicolon.gspass.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {

    @Id
    @Column(length = 45)
    private String id;

    @Column(length = 45)
    private String name;

    @Column(length = 255)
    private String password;

    @Column(length = 5)
    private String gcn;

    @Column(length = 4)
    private String entryYear;

}
