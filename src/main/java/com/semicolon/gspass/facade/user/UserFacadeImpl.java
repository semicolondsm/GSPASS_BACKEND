package com.semicolon.gspass.facade.user;

import com.semicolon.gspass.entity.grade.Grade;
import com.semicolon.gspass.entity.grade.GradeRepository;
import com.semicolon.gspass.entity.gspass.GsPass;
import com.semicolon.gspass.entity.gspass.GsPassRepository;
import com.semicolon.gspass.entity.school.School;
import com.semicolon.gspass.entity.school.SchoolRepository;
import com.semicolon.gspass.entity.user.User;
import com.semicolon.gspass.exception.GsPassAlreadyApplyException;
import com.semicolon.gspass.exception.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final SchoolRepository schoolRepository;
    private final GradeRepository gradeRepository;
    private final GsPassRepository gsPassRepository;

    @Override
    public School findByRandomCode(String randomCode) {
        return schoolRepository.findByRandomCode(randomCode)
                .orElseThrow(SchoolNotFoundException::new);
    }

    @Override
    public School findById(Integer id) {
        return schoolRepository.findById(id)
                .orElseThrow(SchoolNotFoundException::new);
    }

    @Override
    public GsPass save(GsPass gsPass) {
        return gsPassRepository.save(gsPass);
    }

    @Override
    public void useAndSave(GsPass gsPass) {
        gsPassRepository.save(gsPass.use());
    }

    @Override
    public Optional<Grade> findByIdAndSchool(int id, School school) {
        return gradeRepository.findByIdAndSchool(id, school);
    }

    @Override
    public int unUsedPassCount(Grade grade, int id) {
        return gsPassRepository.countByGradeAndIdLessThanAndUsed(grade, id, false);
    }

    @Override
    public int PassCount(Grade grade, int id) {
        return gsPassRepository.countByGradeAndIdLessThan(grade, id);
    }

    @Override
    public Optional<GsPass> findByUser(User user) {
        return gsPassRepository.findByUser(user);
    }

    @Override
    public void deleteAll() {
        gsPassRepository.deleteAll();
    }


}
