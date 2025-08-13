package com.yoim.www.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.yoim.www.model.User;

@Mapper
public interface UserMapper {
	public User providerLogin(HashMap<String, Object> param);
	public void upsert(HashMap<String, Object> param);
}
