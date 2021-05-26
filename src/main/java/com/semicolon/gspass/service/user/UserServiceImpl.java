package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.user.TokenResponse;
import com.semicolon.gspass.entity.user.User;
import com.semicolon.gspass.entity.user.UserRepository;
import com.semicolon.gspass.facade.school.SchoolFacade;
import com.semicolon.gspass.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SchoolFacade schoolFacade;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean nameIsExist(String name) {
        return userRepository.findById(name).isPresent();
    }

    @Override
    public TokenResponse register(RegisterRequest request) {
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

    private TokenResponse generateToken(String name) {
        String accessToken = jwtTokenProvider.generateAccessToken(name);
        String refreshToken = jwtTokenProvider.generateRefreshToken(name);
        return new TokenResponse(accessToken, refreshToken);
    }

}
