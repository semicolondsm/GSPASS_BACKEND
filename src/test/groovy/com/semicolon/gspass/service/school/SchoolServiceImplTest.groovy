package com.semicolon.gspass.service.school

import com.semicolon.gspass.dto.school.SchoolRegisterRequest
import com.semicolon.gspass.entity.school.School
import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.exception.ParseErrorException
import com.semicolon.gspass.exception.SchoolAlreadyExistException
import com.semicolon.gspass.facade.auth.AuthenticationFacade
import spock.lang.Specification

class SchoolServiceImplTest extends Specification {

    def "급식 가져오기"() {
        given:
        def schoolRepository = Mock(SchoolRepository)
        def authenticationFacade = Mock(AuthenticationFacade)
        SchoolService schoolService = new SchoolServiceImpl(schoolRepository, authenticationFacade)

        when:
        schoolService.getMeals(0)

        then:
        schoolRepository.findById(1) >> Optional.of(new School(1, "대덕소프트웨어마이스터고등학교", "7430310", "G10", null, null, null, null, 0, null, null, null))
        authenticationFacade.getSchoolId() >> 1
        notThrown Exception

    }

    def "학교 검색"() {
        given:
        def schoolRepository = Mock(SchoolRepository)
        def authenticationFacade = Mock(AuthenticationFacade)
        SchoolService schoolService = new SchoolServiceImpl(schoolRepository, authenticationFacade)

        when:
        schoolService.getSchools(name as String)

        then:

        notThrown ParseErrorException

        where:
        name << ["해강", "대덕", "부산"]
    }

    def "학교 등록하기"() {
        given:
        def schoolRepository = Mock(SchoolRepository)
        def authenticationFacade = Mock(AuthenticationFacade)
        SchoolService schoolService = new SchoolServiceImpl(schoolRepository, authenticationFacade)

        when:
        schoolService.registerSchool(new SchoolRegisterRequest(schoolCode, scCode, "대덕소프트웨어마이스터고등학교"))

        then:
        notThrown SchoolAlreadyExistException

        where:
        schoolCode | scCode
        "7430310" | "G10"
        "8521131" | "Q10"
    }
}
