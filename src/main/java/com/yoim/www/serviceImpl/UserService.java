package com.yoim.www.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoim.www.model.User;
import com.yoim.www.repository.UserRepository;

@Service
public class UserService{

	@Autowired
	UserRepository userRepository;

	public User providerLogin(HashMap<String, Object> param) {
		String providerId = (String) param.get("providerId");
		return userRepository.findByProviderId(providerId).orElse(null);
	}

	public void upsert(HashMap<String, Object> param) {
		String loginId = (String) param.get("loginId");
		User existing = userRepository.findByLoginId(loginId).orElse(null);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		if (existing != null) {
			// Update existing user
			existing.setLoginPw((String) param.get("loginPw"));
			existing.setUserNm((String) param.get("userNm"));
			existing.setNickNm((String) param.get("nickNm"));
			existing.setEmail((String) param.get("email"));
			existing.setBirthDate((String) param.get("birthDate"));
			existing.setGender((String) param.get("gender"));
			existing.setIntro((String) param.get("intro"));
			existing.setInterests((String) param.get("interests"));
			existing.setPhone((String) param.get("phone"));
			existing.setProviderId((String) param.get("providerId"));
			existing.setProviderType((String) param.get("providerType"));
			existing.setUserDelYn((String) param.get("userDelYn"));
			try {
				existing.setUserDelDate(param.get("userDelDate") != null ? LocalDateTime.parse((String) param.get("userDelDate"), formatter) : null);
			} catch (DateTimeParseException e) {
				existing.setUserDelDate(null);
			}
			Object imgIdObj = param.get("userImgId");
			existing.setUserImgId(imgIdObj instanceof Number ? ((Number) imgIdObj).longValue() : null);
			existing.setUserType((String) param.get("userType"));
			existing.setUpdusrId("SYSTEM");
			existing.setUpdtDt(LocalDateTime.now());
			userRepository.save(existing);
		} else {
			// Insert new user
			User user = new User();
			user.setLoginId(loginId);
			user.setLoginPw((String) param.get("loginPw"));
			user.setUserNm((String) param.get("userNm"));
			user.setNickNm((String) param.get("nickNm"));
			user.setEmail((String) param.get("email"));
			user.setBirthDate((String) param.get("birthDate"));
			user.setGender((String) param.get("gender"));
			user.setIntro((String) param.get("intro"));
			user.setInterests((String) param.get("interests"));
			user.setPhone((String) param.get("phone"));
			user.setProviderId((String) param.get("providerId"));
			user.setProviderType((String) param.get("providerType"));
			user.setUserDelYn((String) param.get("userDelYn"));
			try {
				user.setUserDelDate(param.get("userDelDate") != null ? LocalDateTime.parse((String) param.get("userDelDate"), formatter) : null);
			} catch (DateTimeParseException e) {
				user.setUserDelDate(null);
			}
			Object imgIdObj = param.get("userImgId");
			user.setUserImgId(imgIdObj instanceof Number ? ((Number) imgIdObj).longValue() : null);
			user.setUserType((String) param.get("userType"));
			user.setRegistId("SYSTEM");
			user.setRegistDt(LocalDateTime.now());
			userRepository.save(user);
		}
	}

}
