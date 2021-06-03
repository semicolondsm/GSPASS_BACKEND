package com.semicolon.gspass.security;

import com.semicolon.gspass.exception.InvalidTokenException;
import com.semicolon.gspass.security.auth.AuthDetails;
import com.semicolon.gspass.security.auth.AuthDetailsService;
import com.semicolon.gspass.security.auth.TeacherDetails;
import com.semicolon.gspass.security.auth.TeacherDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.exp.access}")
    private Long accessTokenExpiration;

    @Value("${jwt.exp.refresh}")
    private Long refreshTokenExpiration;


    private final AuthDetailsService authDetailsService;
    private final TeacherDetailsService teacherDetailsService;

    public String generateAccessToken(String id, String role) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .setHeaderParam("typ", "JWT")
                .setSubject(id)
                .claim("type", "access")
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .compact();
    }

    public String generateRefreshToken(String id, String role) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .setHeaderParam("typ", "JWT")
                .setSubject(id)
                .claim("type", "refresh")
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .compact();
    }

    public boolean isRefreshToken(String token) {
        return getTokenBody(token).get("type").equals("refresh");
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(header);
        if(bearer != null && bearer.startsWith(prefix)) {
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateUserToken(String token) {
        try {
            return getTokenBody(token).getExpiration()
                    .after(new Date()) &&
                    getTokenBody(token).getSubject()
                            .equals("user");
        }catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public boolean validateTeacherToken(String token) {
        try {
            return getTokenBody(token).getExpiration()
                    .after(new Date()) &&
                    getTokenBody(token).getSubject()
                            .equals("teacher");
        }catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public Authentication authentication(String token) {
        AuthDetails authDetails = authDetailsService.loadUserByUsername(getName(token));
        return new UsernamePasswordAuthenticationToken(authDetails, "", authDetails.getAuthorities());
    }

    public Authentication teacherAuthentication(String token) {
        TeacherDetails teacherDetails = teacherDetailsService.loadUserByUsername(getName(token));
        return new UsernamePasswordAuthenticationToken(teacherDetails, "", teacherDetails.getAuthorities());
    }

    private String getName(String token) {
        try {
            return getTokenBody(token).getSubject();
        }catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    private String getSigningKey() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

}
