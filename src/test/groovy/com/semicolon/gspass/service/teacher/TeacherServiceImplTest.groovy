package com.semicolon.gspass.service.teacher

import com.semicolon.gspass.dto.LoginRequest
import com.semicolon.gspass.dto.PasswordRequest
import com.semicolon.gspass.dto.teacher.RegisterRequest
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository
import com.semicolon.gspass.entity.school.School
import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.entity.teacher.Teacher
import com.semicolon.gspass.entity.teacher.TeacherRepository
import com.semicolon.gspass.exception.SchoolNotFoundException
import com.semicolon.gspass.exception.TeacherNotFoundException
import com.semicolon.gspass.facade.auth.AuthenticationFacade
import com.semicolon.gspass.security.JwtTokenProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class TeacherServiceImplTest extends Specification {

    def teacherRepository = Mock(TeacherRepository)
    def schoolRepository = Mock(SchoolRepository)
    def refreshTokenRepository = Mock(RefreshTokenRepository)
    def passwordEncoder = new BCryptPasswordEncoder()
    def jwtTokenProvider = Mock(JwtTokenProvider)
    def authenticationFacade = Mock(AuthenticationFacade)

    def "RegisterTeacher"() {
        given:
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, schoolRepository,
                refreshTokenRepository, passwordEncoder, jwtTokenProvider, authenticationFacade)

        when:
        teacherService.registerTeacher(new RegisterRequest(id, password, "testCode"))

        then:
        schoolRepository.findByRandomCode("testCode") >> Optional.of(new School(1, "7430310", "G10", null, null, null, null, 0, null, null))

        notThrown SchoolNotFoundException

        where:
        id      | password
        "test1" | "test1"
        "test2" | "test2"

    }

    def "관리자 로그인"() {
        given:
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, schoolRepository,
                refreshTokenRepository, passwordEncoder, jwtTokenProvider, authenticationFacade)

        when:
        teacherService.login(new LoginRequest(id, password))

        then:
        teacherRepository.findById(id) >> Optional.of(new Teacher(id, passwordEncoder.encode(password), null))

        notThrown TeacherNotFoundException

        where:
        id | password
        "test" | "test"
        "test1" | "test1"
    }

    def "비밀번호 변경"() {
        given:
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, schoolRepository,
                refreshTokenRepository, passwordEncoder, jwtTokenProvider, authenticationFacade)

        when:
        teacherService.changePassword(new PasswordRequest(oldPass, newPass))

        then:
        authenticationFacade.getTeacherId() >> 1.toString()
        teacherRepository.findById("1") >> Optional.of(new Teacher("test", passwordEncoder.encode(oldPass), null))

        where:
        oldPass | newPass
        "1234" | "12345"
        "test" | "test1"
    }

}
