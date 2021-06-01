package com.semicolon.gspass.service.school

import com.semicolon.gspass.dto.school.RegisterRequest
import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.exception.ParseErrorException
import com.semicolon.gspass.exception.SchoolAlreadyExistException
import com.semicolon.gspass.facade.auth.AuthenticationFacade
import spock.lang.Specification

class SchoolServiceImplTest extends Specification {

    def "GetSchools"() {
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

    def "registerSchool"() {
        given:
        def schoolRepository = Mock(SchoolRepository)
        def authenticationFacade = Mock(AuthenticationFacade)
        SchoolService schoolService = new SchoolServiceImpl(schoolRepository, authenticationFacade)

        when:
        schoolService.registerSchool(new RegisterRequest(schoolCode, scCode))

        then:
        notThrown SchoolAlreadyExistException

        where:
        schoolCode | scCode
        "7430310" | "G10"
        "8521131" | "Q10"
    }
}
