package com.semicolon.gspass.facade.school;

import com.semicolon.gspass.entity.school.School;
import com.semicolon.gspass.entity.school.SchoolRepository;
import com.semicolon.gspass.exception.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolFacadeImpl implements SchoolFacade {

    private final SchoolRepository schoolRepository;

    @Override
    public School findByRandomCode(String randomCode) {
        return schoolRepository.findByRandomCode(randomCode)
                .orElseThrow(SchoolNotFoundException::new);
    }
}
