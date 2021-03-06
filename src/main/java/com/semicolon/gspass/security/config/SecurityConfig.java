package com.semicolon.gspass.security.config;

import com.semicolon.gspass.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .cors().and()
                .sessionManagement().disable()
                .csrf().disable();
        http.authorizeRequests()
                .antMatchers("/swagger-ui.html/**", "/swagger-resources/**", "/v3/**", "/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/teacher/register").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/teacher/login").permitAll()
                .antMatchers(HttpMethod.POST, "/refresh").permitAll()
                .antMatchers(HttpMethod.POST, "/school").permitAll()
                .antMatchers(HttpMethod.GET, "/overlap").permitAll()
                .antMatchers(HttpMethod.GET, "/school").permitAll()
                .antMatchers(HttpMethod.POST, "/teacher/refresh").permitAll();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/teacher/school/time").authenticated()
                .antMatchers(HttpMethod.POST, "/teacher/password").authenticated()
                .antMatchers(HttpMethod.POST, "/teacher/grade/time").authenticated()
                .antMatchers(HttpMethod.GET, "/school/information").authenticated()
                .and().apply(new TeacherJwtConfigure(jwtTokenProvider));
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/meals").authenticated()
                .antMatchers(HttpMethod.POST, "/password").authenticated()
                .antMatchers(HttpMethod.POST, "/").authenticated()
                .antMatchers(HttpMethod.GET, "/information").authenticated()
                .and().apply(new UserJwtConfigure(jwtTokenProvider));

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
