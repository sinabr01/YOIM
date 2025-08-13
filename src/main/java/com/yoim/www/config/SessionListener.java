package com.yoim.www.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

//	@Autowired
//	LoginoutService lService;
//	
//	@Autowired
//	MemberService ms;
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
//		HashMap<String, Object> param1 = new HashMap<>();
//		String session_id = (String) se.getSession().getAttribute("session_id");
//		if(session_id != null) {
//			param1.put("MEM_ID", se.getSession().getAttribute("session_id"));
//			List<Member> users = ms.getLogin(param1);
//			HashMap<String, Object> param = new HashMap<>();
//			param.put("LOGIN_ID", se.getSession().getAttribute("session_id"));
//			param.put("L_FLAG", "O");
//			param.put("LOGIN_NM", users.get(0).getMEM_NM());
//			param.put("IP", se.getSession().getAttribute("session_ip"));
//			lService.setInsert(param);
//		}
	}
}
