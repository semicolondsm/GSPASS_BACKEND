package com.semicolon.gspass.entity.gspass;

import com.semicolon.gspass.entity.grade.Grade;
import com.semicolon.gspass.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GsPassRepository extends CrudRepository<GsPass, Integer> {
    Optional<GsPass> findByUser(User user);
    int countByGradeAndIdLessThanAndUsed(Grade grade, int id, boolean isUsed);
}
