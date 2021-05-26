package com.semicolon.gspass.service.user;

import com.semicolon.gspass.dto.user.RegisterRequest;
import com.semicolon.gspass.dto.user.TokenResponse;
import com.semicolon.gspass.entity.school.SchoolRepository;
import com.semicolon.gspass.entity.user.User;
import com.semicolon.gspass.entity.user.UserRepository;
import com.semicolon.gspass.facade.school.SchoolFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SchoolFacade schoolFacade;

    @Override
    public TokenResponse register(RegisterRequest request) {
        userRepository.save(
                User.builder()
                .id(request.getId())
                .entryYear(request.getEntryYear())
                .gcn(request.getGcn())
                .name(request.getName())
                .build()
        );
        // TODO: 2021-05-26  
//        schoolFacade.findByRandomCode(request.getRandomCode())
    }

}
