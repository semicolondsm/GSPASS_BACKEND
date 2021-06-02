package com.semicolon.gspass.service.user

import com.semicolon.gspass.dto.LoginRequest
import com.semicolon.gspass.dto.user.UserRegisterRequest
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository
import com.semicolon.gspass.entity.school.School
import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.entity.user.User
import com.semicolon.gspass.entity.user.UserRepository
import com.semicolon.gspass.error.exception.GsException
import com.semicolon.gspass.exception.UserAlreadyExistException
import com.semicolon.gspass.facade.auth.AuthenticationFacade
import com.semicolon.gspass.facade.school.SchoolFacade
import com.semicolon.gspass.security.JwtTokenProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class UserServiceImplTest extends Specification {

    def schoolRepository = Mock(SchoolRepository)
    def userRepository = Mock(UserRepository)
    def refreshTokenRepository = Mock(RefreshTokenRepository)
    def schoolFacade = Mock(SchoolFacade)
    def authenticationFacade = Mock(AuthenticationFacade)
    def passwordEncoder = new BCryptPasswordEncoder()
    JwtTokenProvider jwtTokenProvider = Mock(JwtTokenProvider)



    def "NameIsExist"() {
    }

    def "Register"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        schoolRepository.save(
                School.builder()
                        .randomCode("test")
                        .scCode("G10")
                        .schoolCode("7430310")
                        .timeLength(10)
                        .build()
        )
        userService.register(new UserRegisterRequest(id, name, password, gcn, entryYear, randomCode))

        then:
        userRepository.findById(id) >> Optional.empty()

        notThrown UserAlreadyExistException

        where:
        id     | name  | password  | gcn    | entryYear | randomCode
        "test" | "테스터" | "1111111" | "0000" | "2020"    | "test"
    }

    def "Login"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        userService.login(new LoginRequest(id, password))

        then:
        userRepository.findById(id) >> Optional.of(new User(id, "test", passwordEncoder.encode(password), null, null, null))

        notThrown GsException

        where:
        id | password
        "test" | "1111111"


    }

    def "TokenRefresh"() {
    }

}
