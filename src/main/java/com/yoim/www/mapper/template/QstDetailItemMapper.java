package com.yoim.www.mapper.template;

import com.yoim.www.model.template.QstDetailItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface QstDetailItemMapper {
	public List<QstDetailItem> qstDetailItemSelect(HashMap<String, Object> param);
	public int qstDetailItemTotalCount(HashMap<String, Object> param);
	public QstDetailItem qstDetailItemView(HashMap<String, Object> param);
	public void qstDetailItemDelete(HashMap<String, Object> param);
	public int qstDetailItemUpsert(QstDetailItem qstDetailItem);
}
