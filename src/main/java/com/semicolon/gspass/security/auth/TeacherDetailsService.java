package com.semicolon.gspass.security.auth;

import com.semicolon.gspass.entity.teacher.TeacherRepository;
import com.semicolon.gspass.exception.TeacherNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    @Override
    public TeacherDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return teacherRepository.findById(username)
                .map(TeacherDetails::new)
                .orElseThrow(TeacherNotFoundException::new);
    }
}
