package com.yoim.www.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoim.www.model.UserAuth;
import com.yoim.www.repository.UserAuthRepository;

@Service
public class UserAuthService{

	@Autowired
	UserAuthRepository userAuthRepository;

	public UserAuth selectOne(HashMap<String, Object> param) {
		String providerId = (String) param.get("providerId");
		return userAuthRepository.findByProviderId(providerId).orElse(null);
	}

	public void upsert(HashMap<String, Object> param) {
		String providerId = (String) param.get("providerId");
		UserAuth existing = userAuthRepository.findByProviderId(providerId).orElse(null);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		if (existing != null) {
			// Update existing
			existing.setProviderType((String) param.get("providerType"));
			existing.setProviderName((String) param.get("providerName"));
			existing.setUpdusrId("SYSTEM");
			try {
				existing.setUpdtDt(param.get("updtDt") != null ? LocalDateTime.parse((String) param.get("updtDt"), formatter) : null);
			} catch (DateTimeParseException e) {
				existing.setUpdtDt(null);
			}
			userAuthRepository.save(existing);
		} else {
			// Insert new
			UserAuth userAuth = new UserAuth();
			userAuth.setProviderId(providerId);
			userAuth.setProviderType((String) param.get("providerType"));
			userAuth.setProviderName((String) param.get("providerName"));
			userAuth.setRegistId("SYSTEM");
			try {
				userAuth.setRegistDt(param.get("registDt") != null ? LocalDateTime.parse((String) param.get("registDt"), formatter) : null);
			} catch (DateTimeParseException e) {
				userAuth.setRegistDt(null);
			}
			userAuthRepository.save(userAuth);
		}
	}

}
