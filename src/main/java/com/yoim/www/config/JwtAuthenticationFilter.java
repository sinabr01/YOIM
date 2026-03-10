package com.yoim.www.config;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);

            if (token != null && tokenProvider.validateToken(token)) {
                String loginId = tokenProvider.getLoginIdFromToken(token);
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginId);

                // 토큰에서 추가 정보 세팅 (DB 정보 덮어쓰기)
                userDetails.setNickname(tokenProvider.getNicknameFromToken(token));
                userDetails.setUserType(tokenProvider.getUserTypeFromToken(token));
                userDetails.setUserId(tokenProvider.getUserIdFromToken(token));
                userDetails.setUsername(tokenProvider.getUserNmFromToken(token));
                userDetails.setEmail(tokenProvider.getEmailFromToken(token));
                userDetails.setBirthDate(tokenProvider.getBirthDateFromToken(token));
                userDetails.setGender(tokenProvider.getGenderFromToken(token));
                userDetails.setIntro(tokenProvider.getIntroFromToken(token));
                userDetails.setInterests(tokenProvider.getInterestsFromToken(token));
                userDetails.setPhone(tokenProvider.getPhoneFromToken(token));
                userDetails.setProviderId(tokenProvider.getProviderIdFromToken(token));
                userDetails.setProviderType(tokenProvider.getProviderTypeFromToken(token));
                userDetails.setUserDelYn(tokenProvider.getUserDelYnFromToken(token));
                userDetails.setUserDelDate(tokenProvider.getUserDelDateFromToken(token));
                userDetails.setUserImgId(tokenProvider.getUserImgIdFromToken(token));
                userDetails.setRegistId(tokenProvider.getRegistIdFromToken(token));
                userDetails.setRegistDt(tokenProvider.getRegistDtFromToken(token));
                userDetails.setUpdusrId(tokenProvider.getUpdusrIdFromToken(token));
                userDetails.setUpdtDt(tokenProvider.getUpdtDtFromToken(token));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(userDetails);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("JWT 검증 실패", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
