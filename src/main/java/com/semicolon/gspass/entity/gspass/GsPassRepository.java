package com.semicolon.gspass.entity.gspass;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GsPassRepository extends CrudRepository<GsPass, Integer> {
}
