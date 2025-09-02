package com.yoim.www.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.serviceImpl.UsersDetailsService;




@Component("authProvider")
public class AuthProvider implements AuthenticationProvider {

	@Autowired
	UsersDetailsService usersDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String id = authentication.getName();
        String password = authentication.getCredentials().toString();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.err.println(passwordEncoder.encode(password));
        CustomUserDetails users = (CustomUserDetails) usersDetailsService.loadUserByUsername(id);
        if (!passwordEncoder.matches(password, users.getPassword())) {
			throw new UsernameNotFoundException("USER NOT FOUND OR NOT MATCH PASSWORD");
		}
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), users.getPassword(), users.getAuthorities());
        
        //setDetails을 함으로써 html에서 th:text="${#authentication.details.nickname}" 이런걸 사용할수있는거임
        result.setDetails(users);
        System.out.println(result.getDetails());
        return result;
	}


	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	
}
