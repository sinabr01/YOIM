package com.yoim.www.mapper.template;

import com.yoim.www.model.template.TemplateQst;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TemplateQstMapper {
	public List<TemplateQst> templateQstSelect(HashMap<String, Object> param);
	public int templateQstTotalCount(HashMap<String, Object> param);
	public TemplateQst templateQstView(HashMap<String, Object> param);
	public void templateQstDelete(HashMap<String, Object> param);
	public int templateQstUpsert(TemplateQst templateQst);
}
