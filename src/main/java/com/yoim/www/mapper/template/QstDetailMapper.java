package com.yoim.www.mapper.template;

import com.yoim.www.model.template.QstDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface QstDetailMapper {
	public List<QstDetail> qstDetailSelect(HashMap<String, Object> param);
	public int qstDetailTotalCount(HashMap<String, Object> param);
	public QstDetail qstDetailView(HashMap<String, Object> param);
	public void qstDetailDelete(HashMap<String, Object> param);
	public int qstDetailUpsert(QstDetail qstDetail);
}
