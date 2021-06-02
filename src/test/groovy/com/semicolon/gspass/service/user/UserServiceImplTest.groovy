package com.semicolon.gspass.service.user

import com.semicolon.gspass.dto.LoginRequest
import com.semicolon.gspass.dto.PasswordRequest
import com.semicolon.gspass.dto.user.UserRegisterRequest
import com.semicolon.gspass.entity.refreshtoken.RefreshToken
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository
import com.semicolon.gspass.entity.school.School
import com.semicolon.gspass.entity.school.SchoolRepository
import com.semicolon.gspass.entity.teacher.Teacher
import com.semicolon.gspass.entity.user.User
import com.semicolon.gspass.entity.user.UserRepository
import com.semicolon.gspass.error.exception.GsException
import com.semicolon.gspass.exception.InvalidPasswordException
import com.semicolon.gspass.exception.InvalidTokenException
import com.semicolon.gspass.exception.UserAlreadyExistException
import com.semicolon.gspass.exception.UserNotFoundException
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

    def "이름이 이미 있을때 true가 반환된다."() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        def result = userService.nameIsExist(name)

        then:
        userRepository.findById(name) >> Optional.of(new User(name, null, null, null, null, null))
        assert result

        where:
        name << ["test", "test1", "test2"]

    }

    def "이름이 없으면 false가 반환된다."() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        def result = userService.nameIsExist(name)

        then:
        userRepository.findById(name) >> Optional.empty()
        assert !result

        where:
        name << ["test", "test1", "test2"]
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

    def "회원가입 아이디 중복"() {
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
        userRepository.findById(id) >> Optional.of(new User(id, name, passwordEncoder.encode(password), null, null, null))
        userRepository.existsById(id) >> userRepository.findById(id).isPresent()

        thrown UserAlreadyExistException

        where:
        id     | name  | password  | gcn    | entryYear | randomCode
        "test" | "테스터" | "1111111" | "0000" | "2020"    | "test"

    }

    def "로그인"() {
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
    
    def "로그인 틀린 비밀번호"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        userService.login(new LoginRequest(id, "wrongPassword"))

        then:
        userRepository.findById(id) >> Optional.of(new User(id, "test", passwordEncoder.encode(password), null, null, null))

        thrown GsException

        where:
        id | password
        "test" | "1111111"
    }

    def "로그인 없는 유저"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        userService.login(new LoginRequest("wrongId", password))

        then:
        userRepository.findById(id) >> Optional.of(new User(id, "test", passwordEncoder.encode(password), null, null, null))
        userRepository.findById("wrongId") >> Optional.empty()

        thrown UserNotFoundException

        where:
        id | password
        "test" | "1111111"
    }

    def "토큰 재발급"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)
        when:
        jwtTokenProvider.isRefreshToken(token) >> true
        refreshTokenRepository.findByRefreshToken(token) >> Optional.of(new RefreshToken("test", token, 100))
        userService.tokenRefresh(token)

        then:
        notThrown InvalidTokenException

        where:
        token << ["refreshToken", "refreshToken1"]
    }

    def "비밀번호 변경"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        userService.changePassword(new PasswordRequest(oldPass, newPass))

        then:
        authenticationFacade.getUserId() >> 1.toString()
        userRepository.findById("1") >> Optional.of(new User("test", null, passwordEncoder.encode(oldPass), null, null, null))

        notThrown GsException

        where:
        oldPass | newPass
        "1234" | "12345"
        "test" | "test1"
    }

    def "비밀번호 변경 잘못된 비밀번호"() {
        given:
        UserService userService = new UserServiceImpl(userRepository, schoolFacade, passwordEncoder
                ,refreshTokenRepository , jwtTokenProvider, authenticationFacade)

        when:
        userService.changePassword(new PasswordRequest(oldPass, "wrongPassword"))

        then:
        authenticationFacade.getUserId() >> 1.toString()
        userRepository.findById("1") >> Optional.of(new User("test", null, passwordEncoder.encode(oldPass), null, null, null))

        notThrown InvalidPasswordException

        where:
        oldPass | newPass
        "1234" | "12345"
        "test" | "test1"
    }

}
