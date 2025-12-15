package com.yoim.www.serviceImpl.template;

import com.yoim.www.mapper.template.CompanyTemplateMapper;
import com.yoim.www.model.template.CompanyTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CompanyTemplateService {

	@Autowired
	CompanyTemplateMapper mapper;

	public List<CompanyTemplate> companyTemplateSelect(HashMap<String, Object> param) {
		return mapper.companyTemplateSelect(param);
	}

	public int companyTemplateTotalCount(HashMap<String, Object> param) {
		return mapper.companyTemplateTotalCount(param);
	}

	public CompanyTemplate companyTemplateView(HashMap<String, Object> param) {
		return mapper.companyTemplateView(param);
	}

	public void companyTemplateDelete(HashMap<String, Object> param) {
		mapper.companyTemplateDelete(param);
	}

	public int companyTemplateUpsert(CompanyTemplate companyTemplate) {
		return mapper.companyTemplateUpsert(companyTemplate);
	}

}
