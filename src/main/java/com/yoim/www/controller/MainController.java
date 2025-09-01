package com.yoim.www.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yoim.www.model.KakaoApi;
import com.yoim.www.model.User;
import com.yoim.www.model.UserAuth;
import com.yoim.www.serviceImpl.NaverOauthService;
import com.yoim.www.serviceImpl.UserAuthService;
import com.yoim.www.serviceImpl.UserService;
import com.yoim.www.util.NaverProfileParser;
import com.yoim.www.util.SecurityLoginUtil;


@Controller
public class MainController {
	
	@Autowired
	private KakaoApi kakaoApi;
	
	@Autowired
	UserAuthService userAuthService;
	
	@Autowired
	UserService userService;
	
	@Autowired 
	SecurityLoginUtil securityLoginUtil;
	
	@Autowired
	NaverOauthService naverOauthService;
	
	NaverProfileParser naverProfileParser;
	
	private static Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
	public String nv_m(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		session.removeAttribute("providerId");
		session.removeAttribute("providerType");
		session.removeAttribute("providerNickNm");
	    return "yoim/main/nv_main";
	}
	
	@RequestMapping(value = "/main/nv_login", method= {RequestMethod.GET, RequestMethod.POST})
    public String nv_login(Model model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
	    model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
		return "yoim/main/nv_login";
    }
	
	@RequestMapping(value = "/main/ts_logout", method= {RequestMethod.GET, RequestMethod.POST})
    public String ts_logout(Model model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/";
    }
	
	@RequestMapping(value = "/main/nv_userJoin", method= {RequestMethod.GET, RequestMethod.POST})
    public String nv_userJoin(Model model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		return "yoim/main/nv_userJoin";
    }
	
	@RequestMapping(value = "/main/ts_signup", method= {RequestMethod.GET, RequestMethod.POST})
    public String ts_signup(Model model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		
		
		//회원가입 완료 페이지로 수정해야됨
		return "yoim/main/nv_main";
    }
	
	
	
}
