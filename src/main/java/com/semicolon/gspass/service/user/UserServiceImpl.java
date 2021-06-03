package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.user.UserRegisterRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.entity.grade.Grade;
import com.semicolon.gspass.entity.gspass.GsPass;
import com.semicolon.gspass.entity.refreshtoken.RefreshToken;
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository;
import com.semicolon.gspass.entity.school.School;
import com.semicolon.gspass.entity.user.User;
import com.semicolon.gspass.entity.user.UserRepository;
import com.semicolon.gspass.exception.*;
import com.semicolon.gspass.facade.auth.AuthenticationFacade;
import com.semicolon.gspass.facade.user.UserFacade;
import com.semicolon.gspass.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationFacade authenticationFacade;

    @Value("${jwt.exp.refresh}")
    private Long refreshTokenExpiration;

    @Override
    public boolean nameIsExist(String name) {
        return userRepository.findById(name).isPresent();
    }

    @Override
    public TokenResponse register(UserRegisterRequest request) {
        if(userRepository.existsById(request.getId())) throw new UserAlreadyExistException();
        userRepository.save(
                User.builder()
                .id(request.getId())
                .entryYear(request.getEntryYear())
                .gcn(request.getGcn())
                .school(userFacade.findByRandomCode(request.getRandomCode()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build()
        );

        return generateToken(request.getId());
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        return generateToken(request.getId());
    }

    @Override
    public TokenResponse tokenRefresh(String token) {
        if(!jwtTokenProvider.isRefreshToken(token)) throw new InvalidTokenException();

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                .map(rToken -> rToken.update(refreshTokenExpiration))
                .orElseThrow(InvalidTokenException::new);

        return new TokenResponse(jwtTokenProvider.generateAccessToken(refreshToken.getId(), "user"), refreshToken.getRefreshToken());
    }

    @Override
    public void changePassword(PasswordRequest request) {
        String userId = authenticationFacade.getUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        userRepository.save(
                user.setPassword(passwordEncoder.encode(request.getNewPassword()))
        );

    }

    @Override
    public void applyGsPass() {
        User user = userRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);

        int gradeId = LocalDate.now().getYear() - Integer.parseInt(user.getEntryYear()) + 1;

        School school = userFacade.findById(user.getSchool().getId());
        Grade grade = userFacade.findByIdAndSchool(gradeId, school)
                .orElseThrow(GradeNotFoundException::new);

        userFacade.save(
                GsPass.builder()
                .user(user)
                .grade(grade)
                .build()
        );
    }

    private TokenResponse generateToken(String id) {
        String accessToken = jwtTokenProvider.generateAccessToken(id, "user");
        String refreshToken = jwtTokenProvider.generateRefreshToken(id, "user");
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(id)
                        .refreshExp(refreshTokenExpiration)
                        .refreshToken(refreshToken)
                        .build()
        );
        return new TokenResponse(accessToken, refreshToken);
    }

}
