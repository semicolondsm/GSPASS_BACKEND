package com.semicolon.gspass.facade.school;

import com.semicolon.gspass.entity.school.School;

public interface SchoolFacade {
    School findByRandomCode(String randomCode);
}
