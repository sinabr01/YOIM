package com.yoim.www.serviceImpl;

import com.yoim.www.mapper.NoticeMapper;
import com.yoim.www.mapper.UserMapper;
import com.yoim.www.model.Notice;
import com.yoim.www.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NoticeService {

	@Autowired
	NoticeMapper mapper;

	public List<Notice> noticeSelect(HashMap<String, Object> param) {
		return mapper.noticeSelect(param);
	}

	public int noticeTotalCount(HashMap<String, Object> param) {
		return mapper.noticeTotalCount(param);
	}

	public Notice noticeView(HashMap<String, Object> param) {
		return mapper.noticeView(param);
	}

	public void noticeDelete(HashMap<String, Object> param) {
		mapper.noticeDelete(param);
	}

	public int noticeUpsert(Notice notice) {
		return mapper.noticeUpsert(notice);
	}

}
