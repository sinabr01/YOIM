package com.yoim.www.mapper.template;

import com.yoim.www.model.template.CompanyTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CompanyTemplateMapper {
	public List<CompanyTemplate> companyTemplateSelect(HashMap<String, Object> param);
	public int companyTemplateTotalCount(HashMap<String, Object> param);
	public CompanyTemplate companyTemplateView(HashMap<String, Object> param);
	public void companyTemplateDelete(HashMap<String, Object> param);
	public int companyTemplateUpsert(CompanyTemplate companyTemplate);
}
