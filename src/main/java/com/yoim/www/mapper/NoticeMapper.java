package com.yoim.www.mapper;

import com.yoim.www.model.Notice;
import com.yoim.www.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface NoticeMapper {
	public List<Notice> noticeSelect(HashMap<String, Object> param);
	public int noticeTotalCount(HashMap<String, Object> param);
	public Notice noticeView(HashMap<String, Object> param);
	public void noticeDelete(HashMap<String, Object> param);
	public void noticeUpsert(HashMap<String, Object> param);
}
