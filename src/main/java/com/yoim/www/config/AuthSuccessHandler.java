package com.yoim.www.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//	@Autowired
//	LoginoutService lService;
//	
//	@Autowired
//	MemberService ms;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
//		HttpSession session = request.getSession();
//		session.setAttribute("session_id", authentication.getName());
//		String ip = request.getRemoteAddr();
//		session.setAttribute("session_ip", ip);
//		ObjectMapper om = new ObjectMapper();
//		String id = request.getParameter("param1");
//		HashMap<String, Object> param1 = new HashMap<>();
//		param1.put("MEM_ID", id);
//		List<Member> users = ms.getLogin(param1);
//		HashMap<String, Object> param = new HashMap<>();
//		param.put("LOGIN_ID", id);
//		param.put("LOGIN_NM", users.get(0).getMEM_NM());
//		param.put("L_FLAG", "L");
//		param.put("IP", ip);
////		lService.setInsert(param);
//		session.setMaxInactiveInterval(1800);
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.getWriter().print(om.writeValueAsString(ResultDto.success()));
		response.getWriter().flush();
	}

}
