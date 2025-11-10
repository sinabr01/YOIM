package com.yoim.www.serviceImpl;

import com.yoim.www.mapper.CmmnCodeMapper;
import com.yoim.www.mapper.NoticeMapper;
import com.yoim.www.model.CmmnCode;
import com.yoim.www.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CmmnCodeService {

	@Autowired
	CmmnCodeMapper mapper;

	public List<CmmnCode> cmmnCodeSelect(String codeTyId) {
		HashMap<String,Object> searchMap = new HashMap<>();
		searchMap.put("codeTyId",codeTyId);
		return mapper.cmmnCodeSelect(searchMap);
	}

}
