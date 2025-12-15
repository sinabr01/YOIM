package com.yoim.www.serviceImpl.template;

import com.yoim.www.mapper.template.TemplateQstMapper;
import com.yoim.www.model.template.TemplateQst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TemplateQstService {

	@Autowired
	TemplateQstMapper mapper;

	public List<TemplateQst> templateQstSelect(HashMap<String, Object> param) {
		return mapper.templateQstSelect(param);
	}

	public int templateQstTotalCount(HashMap<String, Object> param) {
		return mapper.templateQstTotalCount(param);
	}

	public TemplateQst templateQstView(HashMap<String, Object> param) {
		return mapper.templateQstView(param);
	}

	public void templateQstDelete(HashMap<String, Object> param) {
		mapper.templateQstDelete(param);
	}

	public int templateQstUpsert(TemplateQst templateQst) {
		return mapper.templateQstUpsert(templateQst);
	}

}
