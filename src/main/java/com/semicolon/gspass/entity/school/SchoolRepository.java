package com.semicolon.gspass.entity.school;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends CrudRepository<School, Integer> {
    Optional<School> findByRandomCode(String randomCode);
}
