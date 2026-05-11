package com.sports_analysis_app.sports_analysis_app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    @Value("${jwt.access_token_secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh_token_secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access_token_expiration}") // for 24 hours
    private long expiration;

    @Value("${jwt.refresh_token_expiration}") // for 1 month
    private long refreshTokenExpiration;

    public String generateAccessToken(String email, Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(String email, Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractEmailFromAccessToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String extractEmailFromRefreshToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public Long extractUserIdFromAccessToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        return ((Number) Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("userId")).longValue();
    }

    public Long extractUserIdFromRefreshToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        return ((Number) Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("userId")).longValue();
    }

    public boolean validateAccessToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
