package com.yoim.www.serviceImpl.template;

import com.yoim.www.mapper.template.QstDetailItemMapper;
import com.yoim.www.model.template.QstDetailItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class QstDetailItemService {

	@Autowired
	QstDetailItemMapper mapper;

	public List<QstDetailItem> qstDetailItemSelect(HashMap<String, Object> param) {
		return mapper.qstDetailItemSelect(param);
	}

	public int qstDetailItemTotalCount(HashMap<String, Object> param) {
		return mapper.qstDetailItemTotalCount(param);
	}

	public QstDetailItem qstDetailItemView(HashMap<String, Object> param) {
		return mapper.qstDetailItemView(param);
	}

	public void qstDetailItemDelete(HashMap<String, Object> param) {
		mapper.qstDetailItemDelete(param);
	}

	public int qstDetailItemUpsert(QstDetailItem qstDetailItem) {
		return mapper.qstDetailItemUpsert(qstDetailItem);
	}

}
