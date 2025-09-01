package com.yoim.www.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.User;

//예: com.yoim.www.security.SecurityLoginUtil
@Component
public class SecurityLoginUtil {

 public void login(HttpServletRequest request, HttpServletResponse response, User user) {
	// user.getUserType() 값에 따라 권한 분기
	 List<GrantedAuthority> authorities;
	 switch (user.getUserType()) {
	     case "ROLE_HOST":
	         authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_HOST"));
	         break;
	     case "ROLE_ADMIN":
	         authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	         break;
	     default:
	         authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	         break;
	 }

	 CustomUserDetails principal = new CustomUserDetails();
	 principal.setUserId(user.getUserId());
	 principal.setUsername(user.getUserNm());
	 principal.setNickname(user.getNickNm());
	 principal.setUserType(user.getUserType());
	 principal.setUserImg(user.getUserImg());
	 principal.setEmail(user.getEmail());
	 principal.setAuthorities(authorities);

     Authentication auth =
         new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

     SecurityContext context = SecurityContextHolder.createEmptyContext();
     context.setAuthentication(auth);
     SecurityContextHolder.setContext(context);

     new HttpSessionSecurityContextRepository().saveContext(context, request, response);
 }
}

