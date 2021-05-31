package com.semicolon.gspass.service.user

import com.semicolon.gspass.dto.user.RegisterRequest
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository
import com.semicolon.gspass.entity.school.School
import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.entity.user.UserRepository
import com.semicolon.gspass.exception.AlreadyUserExistException
import com.semicolon.gspass.facade.school.SchoolFacade
import com.semicolon.gspass.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceImplTest extends Specification {

    def "NameIsExist"() {
    }

    def "Register"() {
        given:
        def schoolRepository = Mock(SchoolRepository)
        def userRepository = Mock(UserRepository)
        def refreshTokenRepository = Mock(RefreshTokenRepository)
        def schoolFacade = Mock(SchoolFacade)
        def passwordEncoder = Mock(PasswordEncoder)
        def authenticationManager = Mock(AuthenticationManager)
        JwtTokenProvider jwtTokenProvider = Mock(JwtTokenProvider)
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                , authenticationManager, jwtTokenProvider, refreshTokenRepository)

        when:
        schoolRepository.save(
                School.builder()
                        .randomCode("test")
                        .scCode("G10")
                        .schoolCode("7430310")
                        .timeLength(10)
                        .build()
        )
        userService.register(new RegisterRequest(id, name, password, gcn, entryYear, randomCode))

        then:
        userRepository.findById(id) >> Optional.empty()

        notThrown AlreadyUserExistException

        where:
        id     | name  | password  | gcn    | entryYear | randomCode
        "test" | "테스터" | "1111111" | "0000" | "2020"    | "test"
    }

    def "Login"() {
    }

    def "TokenRefresh"() {
    }

}
