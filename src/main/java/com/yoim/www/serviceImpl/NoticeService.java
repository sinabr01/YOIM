package com.yoim.www.serviceImpl;

import com.yoim.www.model.Notice;
import com.yoim.www.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NoticeService {

	@Autowired
	NoticeRepository noticeRepository;

	public List<Notice> noticeSelect(HashMap<String, Object> param) {
		String keyword = (String) param.get("keyword");
		int pageStart = (Integer) param.get("pagestart");
		int pageSize = (Integer) param.get("pagesize");
		return noticeRepository.findNoticesWithPagination(keyword, pageStart, pageSize);
	}

	public int noticeTotalCount(HashMap<String, Object> param) {
		String keyword = (String) param.get("keyword");
		return noticeRepository.countNotices(keyword);
	}

	public Notice noticeView(HashMap<String, Object> param) {
		Long noticeId = ((Number) param.get("noticeId")).longValue();
		return noticeRepository.findById(noticeId).orElse(null);
	}

	public void noticeDelete(HashMap<String, Object> param) {
		Long noticeId = ((Number) param.get("noticeId")).longValue();
		noticeRepository.deleteById(noticeId);
	}

	public void noticeUpsert(Notice notice) {
		noticeRepository.upsertNotice(notice.getTitle(), notice.getContent(), notice.getPinnedYn(), notice.getUseYn(), notice.getRegistId());
	}

}
