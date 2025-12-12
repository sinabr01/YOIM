package com.yoim.www.controller;

import com.yoim.www.model.CompanyTemplate;
import com.yoim.www.serviceImpl.CmmnCodeService;
import com.yoim.www.serviceImpl.CompanyTemplateService;
import com.yoim.www.util.PagingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class CompanyTemplateController {

	@Autowired
	CompanyTemplateService companyTemplateService;

	@Autowired
	CmmnCodeService cmmnCodeService;
	
	private static Logger logger = LoggerFactory.getLogger(CompanyTemplateController.class);

	@RequestMapping("/host/companyTemplate/nv_companyTemplateList")
	public String host_companyTemplate_nv_companyTemplateList(
			@RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			Model model, HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession();

		//  세션 불러오기 (널·빈문자열 방지)
		searchType = (searchType == null)
				? (String) session.getAttribute("companyTemplate_searchType") : searchType;
		keyword = (keyword == null)
				? (String) session.getAttribute("companyTemplate_keyword") : keyword;

		// 페이지 복원
		String pageStr = String.valueOf(session.getAttribute("companyTemplate_currentPage"));
		if (currentPage == 1 && pageStr != null) {
			try { currentPage = Integer.parseInt(pageStr.trim()); }
			catch (NumberFormatException ignore) { currentPage = 1; }
		}

		//  세션 저장
		session.setAttribute("companyTemplate_searchType", searchType);
		session.setAttribute("companyTemplate_keyword", keyword);
		session.setAttribute("companyTemplate_currentPage", currentPage);

		//  파라미터 구성
		int pageSize = 10;
		HashMap<String, Object> param = new HashMap<>();
		param.put("pagestart", (currentPage - 1) * pageSize);
		param.put("pagesize", pageSize);
		if (keyword != null) {
			param.put("searchType", searchType);
			param.put("keyword", keyword);
		}

		//  데이터 조회
		int total = companyTemplateService.companyTemplateTotalCount(param);
		model.addAttribute("plist", companyTemplateService.companyTemplateSelect(param));
		model.addAttribute("page", new PagingAction(currentPage, total, 10, 5, "searchFrm", "").getPagingHtml());
		model.addAttribute("maxnumber", total);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("companyTemplateCode", cmmnCodeService.cmmnCodeSelect("COM_TEM_CODE"));
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);

		return "yoim/host/companyTemplate/nv_companyTemplateList";
	}


	@RequestMapping(value = "/host/companyTemplate/nv_companyTemplateForm")
	public String host_companyTemplate_nv_companyTemplateForm(
			CompanyTemplate companyTemplate,
			Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(companyTemplate!=null && companyTemplate.getTemplateId()!=null){
			HashMap<String, Object> param = new HashMap<>();
			param.put("companyTemplateId", companyTemplate.getTemplateId());
			companyTemplate = companyTemplateService.companyTemplateView(param);
		}else{
			companyTemplate = new CompanyTemplate();
		}
		model.addAttribute("dataVO", companyTemplate);
		return "yoim/host/companyTemplate/nv_companyTemplateForm";
	}

	@RequestMapping(value = "/host/companyTemplate/nv_companyTemplateView")
	public String host_companyTemplate_nv_companyTemplateView(
			CompanyTemplate companyTemplate,
			Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("companyTemplateId", companyTemplate.getTemplateId());
		companyTemplate = companyTemplateService.companyTemplateView(param);
		model.addAttribute("dataVO", companyTemplate);
		return "yoim/host/companyTemplate/nv_companyTemplateView";
	}

	@PostMapping(value = "/host/companyTemplate/ts_companyTemplateUpsert")
	@ResponseBody
	public String host_companyTemplate_ts_companyTemplateUpsert(
			@RequestBody Map<String, Object> paramMap,
			Model model) throws IOException {

		System.out.println(paramMap);

		//companyTemplateService.companyTemplateUpsert(companyTemplate);
		//return "redirect:/host/companyTemplate/nv_companyTemplateView?companyTemplateId=" + companyTemplate.getTemplateId();
		return null;
	}

	@RequestMapping(value = "/host/companyTemplate/ts_companyTemplateDelete")
	public String host_companyTemplate_ts_companyTemplateDelete(
			@RequestParam Map<String, Object> paramMap,
			CompanyTemplate companyTemplate,
			Model model) throws IOException {
		HashMap<String, Object> param = new HashMap<>();
		param.put("companyTemplateId", companyTemplate.getTemplateId());
		companyTemplateService.companyTemplateDelete(param);
		return "redirect:/host/companyTemplate/nv_companyTemplateList";
	}
	

	
	
	
}
