package com.yoim.www.serviceImpl.template;

import com.yoim.www.mapper.template.QstDetailMapper;
import com.yoim.www.model.template.QstDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class QstDetailService {

	@Autowired
	QstDetailMapper mapper;

	public List<QstDetail> qstDetailSelect(HashMap<String, Object> param) {
		return mapper.qstDetailSelect(param);
	}

	public int qstDetailTotalCount(HashMap<String, Object> param) {
		return mapper.qstDetailTotalCount(param);
	}

	public QstDetail qstDetailView(HashMap<String, Object> param) {
		return mapper.qstDetailView(param);
	}

	public void qstDetailDelete(HashMap<String, Object> param) {
		mapper.qstDetailDelete(param);
	}

	public int qstDetailUpsert(QstDetail qstDetail) {
		return mapper.qstDetailUpsert(qstDetail);
	}

}
