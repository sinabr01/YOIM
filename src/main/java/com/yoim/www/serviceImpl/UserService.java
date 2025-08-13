package com.yoim.www.serviceImpl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoim.www.mapper.UserMapper;
import com.yoim.www.model.User;

@Service
public class UserService{

	@Autowired
	UserMapper mapper;

	public User providerLogin(HashMap<String, Object> param) {
		return mapper.providerLogin(param);
	}
	public void upsert(HashMap<String, Object> param) {
		mapper.upsert(param);
	}

}
