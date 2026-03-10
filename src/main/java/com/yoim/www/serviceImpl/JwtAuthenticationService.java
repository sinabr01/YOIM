package com.yoim.www.serviceImpl;

import com.yoim.www.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtAuthenticationService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public Long validateTokenAndGetUserId(HttpServletRequest request) throws Exception {
        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new Exception("Authorization header missing or invalid");
        }
        String token = authHeader.substring(7); // Remove "Bearer "

        // Validate token
        if (!tokenProvider.validateToken(token)) {
            throw new Exception("Invalid token");
        }

        // Extract userId from token
        Long userId = tokenProvider.getUserIdFromToken(token);
        if (userId == null) {
            throw new Exception("Invalid token: userId not found");
        }

        return userId;
    }
}
