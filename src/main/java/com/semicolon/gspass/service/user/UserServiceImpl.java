package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.user.LoginRequest;
import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.TokenResponse;
import com.semicolon.gspass.entity.refreshtoken.RefreshToken;
import com.semicolon.gspass.entity.refreshtoken.RefreshTokenRepository;
import com.semicolon.gspass.entity.user.User;
import com.semicolon.gspass.entity.user.UserRepository;
import com.semicolon.gspass.exception.UserAlreadyExistException;
import com.semicolon.gspass.exception.InvalidTokenException;
import com.semicolon.gspass.exception.UserNotFoundException;
import com.semicolon.gspass.facade.school.SchoolFacade;
import com.semicolon.gspass.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SchoolFacade schoolFacade;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.exp.refresh}")
    private Long refreshTokenExpiration;

    @Override
    public boolean nameIsExist(String name) {
        return userRepository.findById(name).isPresent();
    }

    @Override
    public TokenResponse register(RegisterRequest request) {
        if(userRepository.findById(request.getId()).isPresent()) throw new UserAlreadyExistException();
        userRepository.save(
                User.builder()
                .id(request.getId())
                .entryYear(request.getEntryYear())
                .gcn(request.getGcn())
                .name(request.getName())
                .school(schoolFacade.findByRandomCode(request.getRandomCode()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build()
        );

        return generateToken(request.getName());
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        userRepository.findById(request.getId())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElseThrow(UserNotFoundException::new);
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

    private TokenResponse generateToken(String name) {
        String accessToken = jwtTokenProvider.generateAccessToken(name, "user");
        String refreshToken = jwtTokenProvider.generateRefreshToken(name, "user");
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(name)
                        .refreshExp(refreshTokenExpiration)
                        .refreshToken(refreshToken)
                        .build()
        );
        return new TokenResponse(accessToken, refreshToken);
    }

}
