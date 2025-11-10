package com.yoim.www.mapper;

import com.yoim.www.model.CmmnCode;
import com.yoim.www.model.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CmmnCodeMapper {
	public List<CmmnCode> cmmnCodeSelect(HashMap<String, Object> param);
}
