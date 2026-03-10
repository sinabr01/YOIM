package com.yoim.www.util;

import com.yoim.www.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:your-secret-key-change-this-to-secure-key-at-least-32-characters-long}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    public String generateToken(CustomUserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(userDetails.getLoginId())
                .claim("nickname", userDetails.getNickname())
                .claim("userType", userDetails.getUserType())
                .claim("userId", userDetails.getUserId())
                .claim("userNm", userDetails.getUsername())
                .claim("email", userDetails.getEmail())
                .claim("birthDate", userDetails.getBirthDate())
                .claim("gender", userDetails.getGender())
                .claim("intro", userDetails.getIntro())
                .claim("interests", userDetails.getInterests())
                .claim("phone", userDetails.getPhone())
                .claim("providerId", userDetails.getProviderId())
                .claim("providerType", userDetails.getProviderType())
                .claim("userDelYn", userDetails.getUserDelYn())
                .claim("userDelDate", userDetails.getUserDelDate())
                .claim("userImgId", userDetails.getUserImgId())
                .claim("registId", userDetails.getRegistId())
                .claim("registDt", userDetails.getRegistDt())
                .claim("updusrId", userDetails.getUpdusrId())
                .claim("updtDt", userDetails.getUpdtDt())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getLoginIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return ((Number) getClaimsFromToken(token).get("userId")).longValue();
    }

    public String getNicknameFromToken(String token) {
        return (String) getClaimsFromToken(token).get("nickname");
    }

    public String getUserTypeFromToken(String token) {
        return (String) getClaimsFromToken(token).get("userType");
    }

    public String getUserNmFromToken(String token) {
        return (String) getClaimsFromToken(token).get("userNm");
    }

    public String getEmailFromToken(String token) {
        return (String) getClaimsFromToken(token).get("email");
    }

    public String getBirthDateFromToken(String token) {
        return (String) getClaimsFromToken(token).get("birthDate");
    }

    public String getGenderFromToken(String token) {
        return (String) getClaimsFromToken(token).get("gender");
    }

    public String getIntroFromToken(String token) {
        return (String) getClaimsFromToken(token).get("intro");
    }

    public String getInterestsFromToken(String token) {
        return (String) getClaimsFromToken(token).get("interests");
    }

    public String getPhoneFromToken(String token) {
        return (String) getClaimsFromToken(token).get("phone");
    }

    public String getProviderIdFromToken(String token) {
        return (String) getClaimsFromToken(token).get("providerId");
    }

    public String getProviderTypeFromToken(String token) {
        return (String) getClaimsFromToken(token).get("providerType");
    }

    public String getUserDelYnFromToken(String token) {
        return (String) getClaimsFromToken(token).get("userDelYn");
    }

    public String getUserDelDateFromToken(String token) {
        return (String) getClaimsFromToken(token).get("userDelDate");
    }

    public Long getUserImgIdFromToken(String token) {
        Object imgId = getClaimsFromToken(token).get("userImgId");
        return imgId != null ? ((Number) imgId).longValue() : null;
    }

    public String getRegistIdFromToken(String token) {
        return (String) getClaimsFromToken(token).get("registId");
    }

    public String getRegistDtFromToken(String token) {
        return (String) getClaimsFromToken(token).get("registDt");
    }

    public String getUpdusrIdFromToken(String token) {
        return (String) getClaimsFromToken(token).get("updusrId");
    }

    public String getUpdtDtFromToken(String token) {
        return (String) getClaimsFromToken(token).get("updtDt");
    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
