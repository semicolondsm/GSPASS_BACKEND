package com.semicolon.gspass.entity.grade;

import com.semicolon.gspass.entity.school.School;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Integer> {
    Optional<Grade> findByIdAndSchool(Integer id, School school);
}
