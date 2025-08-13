package com.yoim.www.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yoim.www.model.UserAuth;

@Mapper
public interface UserAuthMapper {
	public UserAuth selectOne(HashMap<String, Object> param);
	public void upsert(HashMap<String, Object> param);
}
