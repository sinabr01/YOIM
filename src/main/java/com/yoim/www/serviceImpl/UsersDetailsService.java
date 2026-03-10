package com.yoim.www.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.User;
import com.yoim.www.repository.UserRepository;


@Service
public class UsersDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLoginId(username).orElse(null);
		if (user==null) {
        	throw new UsernameNotFoundException("USER NOT FOUND OR NOT MATCH PASSWORD");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		CustomUserDetails principal = new CustomUserDetails();
		principal.setUserId(user.getUserId().longValue());
		principal.setLoginId(user.getLoginId());
		principal.setUsername(user.getUserNm());
		principal.setNickname(user.getNickNm());
		principal.setEmail(user.getEmail());
		principal.setPassword(user.getLoginPw());
		principal.setUserType(user.getUserType());
		principal.setBirthDate(user.getBirthDate());
		principal.setGender(user.getGender());
		principal.setIntro(user.getIntro());
		principal.setInterests(user.getInterests());
		principal.setPhone(user.getPhone());
		principal.setProviderId(user.getProviderId());
		principal.setProviderType(user.getProviderType());
		principal.setUserDelYn(user.getUserDelYn());
		principal.setUserDelDate(user.getUserDelDate() != null ? user.getUserDelDate().format(formatter) : null);
		principal.setUserImgId(user.getUserImgId() != null ? user.getUserImgId().longValue() : null);
		principal.setRegistId(user.getRegistId());
		principal.setRegistDt(user.getRegistDt() != null ? user.getRegistDt().format(formatter) : null);
		principal.setUpdusrId(user.getUpdusrId());
		principal.setUpdtDt(user.getUpdtDt() != null ? user.getUpdtDt().format(formatter) : null);
		principal.setEnabled(!"Y".equals(user.getUserDelYn())); // Assuming userDelYn 'Y' means deleted
		principal.setAccountNonExpired(true);
		principal.setAccountNonLocked(true);
		principal.setCredentialsNonExpired(true);
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
		grantedAuthorityList.add(new SimpleGrantedAuthority(user.getUserType()));
		principal.setAuthorities(grantedAuthorityList);
		return principal;
	}

}
