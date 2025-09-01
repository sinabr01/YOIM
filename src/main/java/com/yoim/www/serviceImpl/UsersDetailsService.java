package com.yoim.www.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yoim.www.mapper.UserMapper;
import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.User;


@Service
public class UsersDetailsService implements UserDetailsService{

	@Autowired
	UserMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("loginId", username);
		User user = mapper.userLogin(param);
		if (user==null) {
        	throw new UsernameNotFoundException("USER NOT FOUND OR NOT MATCH PASSWORD");
        }
		CustomUserDetails principal = new CustomUserDetails();
		principal.setUserId(user.getUserId());
		principal.setUsername(user.getUserNm());
		principal.setNickname(user.getNickNm());
		principal.setUserType(user.getUserType());
		principal.setEmail(user.getEmail());
		principal.setPassword(user.getLoginPw());
		principal.setUserImg(user.getUserImg());
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
		grantedAuthorityList.add(new SimpleGrantedAuthority(user.getUserType()));
		principal.setAuthorities(grantedAuthorityList);
		return principal;
	}

}
