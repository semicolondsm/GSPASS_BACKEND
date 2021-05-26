package com.semicolon.gspass.security.jwt;

import com.semicolon.gspass.security.jwt.auth.AuthDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

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


    private AuthDetailsService authDetailsService;

    public String generateAccessToken(String id) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .setHeaderParam("typ", "JWT")
                .setSubject(id)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .compact();
    }

    public String generateRefreshToken(String id) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .setHeaderParam("typ", "JWT")
                .setSubject(id)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .compact();
    }

    private String getSigningKey() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

}
