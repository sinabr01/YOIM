package com.yoim.www.controller;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.util.JwtTokenProvider;
import com.yoim.www.repository.UserRepository;
import com.yoim.www.model.User;
import com.yoim.www.serviceImpl.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthenticationService jwtAuthService;

    private CustomUserDetails convertUserToCustomUserDetails(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserId(user.getUserId());
        userDetails.setLoginId(user.getLoginId());
        userDetails.setUsername(user.getUserNm());
        userDetails.setNickname(user.getNickNm());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getLoginPw());
        userDetails.setUserType(user.getUserType());
        userDetails.setBirthDate(user.getBirthDate());
        userDetails.setGender(user.getGender());
        userDetails.setIntro(user.getIntro());
        userDetails.setInterests(user.getInterests());
        userDetails.setPhone(user.getPhone());
        userDetails.setProviderId(user.getProviderId());
        userDetails.setProviderType(user.getProviderType());
        userDetails.setUserDelYn(user.getUserDelYn());
        userDetails.setUserDelDate(user.getUserDelDate() != null ? user.getUserDelDate().format(formatter) : null);
        userDetails.setUserImgId(user.getUserImgId());
        userDetails.setRegistId(user.getRegistId());
        userDetails.setRegistDt(user.getRegistDt() != null ? user.getRegistDt().format(formatter) : null);
        userDetails.setUpdusrId(user.getUpdusrId());
        userDetails.setUpdtDt(user.getUpdtDt() != null ? user.getUpdtDt().format(formatter) : null);
        userDetails.setEnabled(!"Y".equals(user.getUserDelYn()));
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);
        return userDetails;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLoginId(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", userDetails);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        CustomUserDetails tokenUser = (CustomUserDetails) authentication.getPrincipal();
        User dbUser = userRepository.findById(tokenUser.getUserId()).orElse(null);
        if (dbUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        
        return ResponseEntity.ok(convertUserToCustomUserDetails(dbUser));
    }

    @PutMapping("/me")
    @Transactional
    public ResponseEntity<?> updateCurrentUser(@RequestBody ProfileUpdateRequest updateRequest, HttpServletRequest request) {
        try {
            // Validate token and extract userId
            Long userId = jwtAuthService.validateTokenAndGetUserId(request);

            // Update user
            userRepository.updateUser(userId, updateRequest.getUsername(), updateRequest.getNickname(), updateRequest.getIntro(), updateRequest.getGender(), updateRequest.getBirthDate(), "SYSTEM");

            // Fetch updated user data
            User updatedUser = userRepository.findById(userId).orElse(null);
            if (updatedUser == null) {
                return ResponseEntity.status(404).body("User not found after update");
            }

            return ResponseEntity.ok(convertUserToCustomUserDetails(updatedUser));

        } catch (Exception e) {
            if (e.getMessage().contains("Authorization header") || e.getMessage().contains("Invalid token")) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @Data
    public static class LoginRequest {
        @JsonProperty("login_id")
        private String loginId;
        private String password;
    }

    @Data
    public static class ProfileUpdateRequest {
        private String username;
        private String nickname;
        private String intro;
        private String gender;
        private String birthDate;
    }
}
