package com.yoim.www.controller;

import com.yoim.www.model.KakaoApi;
import com.yoim.www.model.Notice;
import com.yoim.www.model.User;
import com.yoim.www.serviceImpl.*;
import com.yoim.www.util.NaverProfileParser;
import com.yoim.www.util.PagingAction;
import com.yoim.www.util.SecurityLoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class NoticeController {

	@Autowired
	NoticeService noticeService;

	@Autowired
	CmmnCodeService cmmnCodeService;
	
	private static Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@RequestMapping(value = "/admin/notice/nv_noticeList")
	public String admin_notice_nv_noticeList(
			@RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage,
			Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HashMap<String,Object> param = new HashMap<>();
		param.put("pagestart", (currentPage-1)*10);
		param.put("pagesize", 10);
		if(keyword!=null){
			param.put("searchType", searchType);
			param.put("keyword", keyword);
		}
		int total = noticeService.noticeTotalCount(param);
		PagingAction page = new PagingAction(
				currentPage,
				total,
				10,
				5,
				"searchFrm",
				"");
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("plist", noticeService.noticeSelect(param));
		model.addAttribute("maxnumber", total);
		model.addAttribute("page", page.getPagingHtml());
		model.addAttribute("noticeCode", cmmnCodeService.cmmnCodeSelect("NOTICE_CODE"));
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
	    return "yoim/admin/notice/nv_noticeList";
	}

	@RequestMapping(value = "/admin/notice/nv_noticeForm")
	public String admin_notice_nv_noticeForm(
			Notice notice,
			Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(notice!=null && notice.getNoticeId()!=null){
			HashMap<String, Object> param = new HashMap<>();
			param.put("noticeId", notice.getNoticeId());
			notice = noticeService.noticeView(param);
		}else{
			notice = new Notice();
		}
		model.addAttribute("dataVO", notice);
		return "yoim/admin/notice/nv_noticeForm";
	}

	@RequestMapping(value = "/admin/notice/nv_noticeView")
	public String admin_notice_nv_noticeView(
			Notice notice,
			Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("noticeId", notice.getNoticeId());
		notice = noticeService.noticeView(param);
		model.addAttribute("dataVO", notice);
		return "yoim/admin/notice/nv_noticeView";
	}

	@RequestMapping(value = "/admin/notice/ts_noticeUpsert")
	public String admin_notice_ts_noticeUpsert(
			@RequestParam Map<String, Object> paramMap,
			Notice notice,
			Model model) throws IOException {
		notice.setPinnedYn("N");
		notice.setUseYn("Y");
		noticeService.noticeUpsert(notice);
		model.addAttribute("dataVO", notice);
		return "yoim/admin/notice/nv_noticeView";
	}

	@RequestMapping(value = "/admin/notice/ts_noticeDelete")
	public String admin_notice_ts_noticeDelete(
			@RequestParam Map<String, Object> paramMap,
			Notice notice,
			Model model) throws IOException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("noticeId", notice.getNoticeId());
		noticeService.noticeDelete(param);
		return "redirect:/admin/notice/nv_noticeList";
	}
	

	
	
	
}
