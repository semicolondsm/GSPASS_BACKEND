package com.semicolon.gspass.service.school

import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.exception.ParseErrorException
import spock.lang.Specification

class SchoolServiceImplTest extends Specification {

    def "GetSchools"() {
        given:
        def schoolRepository = Mock(SchoolRepository)
        SchoolService schoolService = new SchoolServiceImpl(schoolRepository)

        when:
        schoolService.getSchools(name as String)

        then:
        notThrown ParseErrorException

        where:
        name << ["해강", "대덕", "부산"]
    }
}
