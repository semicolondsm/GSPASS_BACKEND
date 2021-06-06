package com.semicolon.gspass.facade.user;

import com.semicolon.gspass.entity.grade.Grade;
import com.semicolon.gspass.entity.gspass.GsPass;
import com.semicolon.gspass.entity.school.School;
import com.semicolon.gspass.entity.user.User;

import java.util.Optional;

public interface UserFacade {
    School findByRandomCode(String randomCode);
    School findById(Integer id);
    GsPass save(GsPass gsPass);
    Optional<Grade> findByIdAndSchool(int id, School school);
    int unUsedPassCount(Grade grade, int id);
    int PassCount(Grade grade, int id);
    Optional<GsPass> findByUser(User user);
}
