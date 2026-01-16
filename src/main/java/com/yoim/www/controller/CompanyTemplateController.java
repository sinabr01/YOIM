package com.yoim.www.controller;

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.template.CompanyTemplate;
import com.yoim.www.serviceImpl.CmmnCodeService;
import com.yoim.www.serviceImpl.template.CompanyTemplateService;
import com.yoim.www.serviceImpl.template.QstDetailItemService;
import com.yoim.www.serviceImpl.template.QstDetailService;
import com.yoim.www.serviceImpl.template.TemplateQstService;
import com.yoim.www.util.PagingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CompanyTemplateController {

	@Autowired
	CompanyTemplateService companyTemplateService;

	@Autowired
	QstDetailService qstDetailService;

	@Autowired
	QstDetailItemService qstDetailItemService;

	@Autowired
	TemplateQstService templateQstService;

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
			Model model,
			Authentication authentication) throws IOException {
		CustomUserDetails user = (CustomUserDetails) authentication.getDetails();

		System.out.println(paramMap);

		// 제목
		String title = (String) paramMap.get("title");

		CompanyTemplate companyTemplate = new CompanyTemplate();
		companyTemplate.setTitle(title);
		companyTemplate.setRegistId(user.getLoginId());
		companyTemplate.setCompanyId(1L);
		int templateId = companyTemplateService.companyTemplateUpsert(companyTemplate);

		System.out.println(companyTemplate.getTemplateId());


		// 질문 리스트
		List<Map<String, Object>> questions = (List<Map<String, Object>>) paramMap.get("questions");

		for (Map<String, Object> q : questions) {

			String question = (String) q.get("question");
			String requiredYn = (String) q.get("requiredYn");
			String type = (String) q.get("type");
			int orderNo = (int) q.get("orderNo");

			System.out.println("===== 질문 =====");
			System.out.println(question + " / 필수:" + requiredYn + " / 형태:" + type);

			// 상세 리스트
			List<Map<String, Object>> details = (List<Map<String, Object>>) q.get("details");

			for (Map<String, Object> d : details) {

				String option = (String) d.get("qstOption");
				int detailOrder = (int) d.get("orderNo");

				System.out.println("  --- 상세 옵션: " + option);

				// 상세에 딸린 아이템 리스트 (예: 라디오 항목)
				List<Map<String, Object>> items =
						(List<Map<String, Object>>) d.get("items");

				for (Map<String, Object> item : items) {

					String itemName = (String) item.get("itemName");
					int itemOrder = (int) item.get("orderNo");

					System.out.println("    * " + itemOrder + " : " + itemName);
				}
			}
		}

		//return Map.of("result", true);


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
