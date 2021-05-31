package com.semicolon.gspass.facade.auth;

import com.semicolon.gspass.entity.teacher.Teacher;
import com.semicolon.gspass.entity.user.User;
import com.semicolon.gspass.exception.TeacherNotFoundException;
import com.semicolon.gspass.exception.UserNotFoundException;
import com.semicolon.gspass.security.auth.AuthDetails;
import com.semicolon.gspass.security.auth.TeacherDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUserId() {
        try{
            return this.getAuthentication().getName();
        } catch (Exception e){
            throw new UserNotFoundException();
        }
    }

    public int getSchoolId() {
        try{
            return ((AuthDetails)this.getAuthentication().getPrincipal()).getUser().getSchool().getId();
        } catch (Exception e){
            throw new UserNotFoundException();
        }
    }

    public Teacher getTeacher() {
        try{
            return ((TeacherDetails)this.getAuthentication().getPrincipal()).getTeacher();
        } catch (Exception e) {
            throw new TeacherNotFoundException();
        }
    }

}
