package com.yoim.www.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.User;
import com.yoim.www.serviceImpl.UsersDetailsService;


@Component
public class SecurityLoginUtil {

	@Autowired
	UsersDetailsService usersDetailsService;

	public void login(HttpServletRequest request, HttpServletResponse response, User user) {
		CustomUserDetails users = (CustomUserDetails) usersDetailsService.loadUserByUsername(user.getLoginId());

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(users, null,
				users.getAuthorities());

		// (선택) 통일 원하면 이것도 넣어두기
		auth.setDetails(users);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);
		new HttpSessionSecurityContextRepository().saveContext(context, request, response);

	}
}
