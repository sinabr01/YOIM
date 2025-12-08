package com.yoim.www.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SearchInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();

		String searchType = request.getParameter("searchType");
		String keyword = request.getParameter("keyword");
		String currentPage = request.getParameter("currentPage");

		String uri = request.getRequestURI();

		// ===== Notice =====
		if (uri.contains("/admin/notice")) {
			if (searchType != null) session.setAttribute("notice_searchType", searchType);
			if (keyword != null) session.setAttribute("notice_keyword", keyword);
			if (currentPage != null) session.setAttribute("notice_currentPage", currentPage);
		}

		// ===== CompanyTemplate =====
		if (uri.contains("/host/companyTemplate")) {
			if (searchType != null) session.setAttribute("companyTemplate_searchType", searchType);
			if (keyword != null) session.setAttribute("companyTemplate_keyword", keyword);
			if (currentPage != null) session.setAttribute("companyTemplate_currentPage", currentPage);
		}

		return true;
	}
}
