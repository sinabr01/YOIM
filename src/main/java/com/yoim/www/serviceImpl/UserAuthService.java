package com.yoim.www.serviceImpl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoim.www.mapper.UserAuthMapper;
import com.yoim.www.model.UserAuth;

@Service
public class UserAuthService{

	@Autowired
	UserAuthMapper mapper;

	public UserAuth selectOne(HashMap<String, Object> param) {
		return mapper.selectOne(param);
	}
	public void upsert(HashMap<String, Object> param) {
		mapper.upsert(param);
	}

}
