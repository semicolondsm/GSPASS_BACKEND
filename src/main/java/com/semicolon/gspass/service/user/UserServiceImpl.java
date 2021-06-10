package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.LoginRequest;
import com.semicolon.gspass.dto.PasswordRequest;
import com.semicolon.gspass.dto.user.GsPassResponse;
import com.semicolon.gspass.dto.user.GsPassTimeResponse;
import com.semicolon.gspass.dto.user.UserInformationResponse;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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
        if (userRepository.existsById(request.getId())) throw new UserAlreadyExistException();
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

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        return generateToken(request.getId());
    }

    @Override
    public TokenResponse tokenRefresh(String token) {
        if (!jwtTokenProvider.isRefreshToken(token) || !jwtTokenProvider.validateUserToken(token)) throw new InvalidTokenException();

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                .map(rToken -> rToken.update(refreshTokenExpiration))
                .orElseThrow(InvalidTokenException::new);

        return new TokenResponse(jwtTokenProvider.generateAccessToken(refreshToken.getId(), "user"), refreshToken.getRefreshToken());
    }

    @Override
    public void changePassword(PasswordRequest request) {
        String userId = authenticationFacade.getUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        userRepository.save(
                user.setPassword(passwordEncoder.encode(request.getNewPassword()))
        );

    }

    @Override
    public GsPassResponse applyGsPass() {
        User user = userRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);

        int gradeId = LocalDate.now().getYear() - Integer.parseInt(user.getEntryYear()) + 1;

        School school = userFacade.findById(user.getSchool().getId());

        if (!(
                (school.getBreakfastPeriod() != null &&
                school.getBreakfastPeriod().toLocalTime().isBefore(LocalTime.now()) &&
                school.getBreakfastPeriod().toLocalTime().plusMinutes(school.getTimeLength()).isAfter(LocalTime.now())) ||
                (school.getLunchPeriod() != null &&
                        school.getLunchPeriod().toLocalTime().isBefore(LocalTime.now()) &&
                        school.getLunchPeriod().toLocalTime().plusMinutes(school.getTimeLength()).isAfter(LocalTime.now())) ||
                (school.getDinnerPeriod() != null &&
                        school.getDinnerPeriod().toLocalTime().isBefore(LocalTime.now()) &&
                        school.getDinnerPeriod().toLocalTime().plusMinutes(school.getTimeLength()).isAfter(LocalTime.now()))
                )) throw new NotGsPassApplyTimeException();

            Grade grade = userFacade.findByIdAndSchool(gradeId, school)
                    .orElseThrow(GradeNotFoundException::new);

        userFacade.save(
                GsPass.builder()
                        .user(user)
                        .grade(grade)
                        .used(false)
                        .build()
        );

        return getPassInfo();
    }

    @Override
    public UserInformationResponse getUserInfo() {
        User user = userRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);
        return new UserInformationResponse(user.getSchool().getSchoolName(), user.getGcn());
    }

    @Override
    public GsPassResponse getPassInfo() {
        User user = userRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);
        int gradeId = LocalDate.now().getYear() - Integer.parseInt(user.getEntryYear()) + 1;

        School school = userFacade.findById(user.getSchool().getId());
        Grade grade = userFacade.findByIdAndSchool(gradeId, school)
                .orElseThrow(GradeNotFoundException::new);
        GsPass gsPass = userFacade.findByUser(user)
                .orElseThrow(GsPassNotFoundException::new);
        if(gsPass.isUsed()) throw new GsPassNotFoundException();
        int count = userFacade.unUsedPassCount(grade, gsPass.getId());
        int allCount = userFacade.PassCount(grade, gsPass.getId());
        if (school.getDinnerPeriod() != null && school.getDinnerPeriod().toLocalTime().isBefore(LocalTime.now())) {
            return new GsPassResponse(count, grade.getDinner().toLocalTime().plusSeconds(5 * (allCount+1)));
        } else if (school.getLunchPeriod() != null && school.getLunchPeriod().toLocalTime().isBefore(LocalTime.now())) {
            return new GsPassResponse(count, grade.getLunch().toLocalTime().plusSeconds(5 * (allCount+1)));
        } else if (school.getBreakfastPeriod() != null && school.getBreakfastPeriod().toLocalTime().isBefore(LocalTime.now())) {
            return new GsPassResponse(count, grade.getBreakfast().toLocalTime().plusSeconds(5 * (allCount+1)));
        } else throw new GsPassNotFoundException();
    }

    @Override
    public GsPassTimeResponse getNextGsPassTime() {
        User user = userRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);

        School school = userFacade.findById(user.getSchool().getId());

        if(school.getBreakfastPeriod() != null && school.getBreakfastPeriod().toLocalTime().isAfter(LocalTime.now()))
            return new GsPassTimeResponse(school.getBreakfastPeriod());
        else if(school.getLunchPeriod() != null && school.getLunchPeriod().toLocalTime().isAfter(LocalTime.now()))
            return new GsPassTimeResponse(school.getLunchPeriod());
        else if(school.getDinnerPeriod() != null && school.getDinnerPeriod().toLocalTime().isAfter(LocalTime.now()))
            return new GsPassTimeResponse(school.getDinnerPeriod());
        else return null;

    }

    @Override
    public void useGsPass() {
        User user = userRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);

        GsPass gsPass = userFacade.findByUser(user)
                .orElseThrow(GsPassNotFoundException::new);

        userFacade.save(gsPass.use());
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

    @Scheduled(cron = "0 0 9,2,20 * * *", zone = "Asia/Seoul")
    public void deleteGsPass() {
        userFacade.deleteAll();
    }

}
