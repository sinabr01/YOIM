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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
public class IndexController {
	
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
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
	public ResponseEntity<Void> quiet() {
	  return ResponseEntity.noContent().build(); // 204
	}
	
	@RequestMapping(value = "/fileTest", method = {RequestMethod.GET, RequestMethod.POST})
	public String fileTest(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
	    return "yoim/fileTest";
	}
	
	
	@RequestMapping(value = "/test", method= {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model,HttpServletRequest request,
    		@AuthenticationPrincipal UserDetails user,
			HttpServletResponse response) throws IOException{
		
		System.out.println(user.toString());
		
		String username2 = (user != null) ? user.getUsername() : null;
		
		System.out.println(username2);
		return "yoim/login";
    }
	
	@RequestMapping(value = "/naverLogin", method= {RequestMethod.GET, RequestMethod.POST})
    public String naverLogin(Model model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		return "yoim/naverLoginCheck";
		
	}
	
	@PostMapping("/naverLogin/token")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> naverLoginToken(
	        @RequestBody Map<String,String> body,
	        HttpServletRequest request,
	        HttpServletResponse response) {

	    String accessToken = body.get("accessToken");
	    String state = body.get("state"); // 있으면 검증

	    if (accessToken == null || accessToken.isEmpty()) {
	        Map<String,Object> fail = new HashMap<String,Object>();
	        fail.put("ok", false);
	        fail.put("message", "missing accessToken");
	        return ResponseEntity.badRequest().body(fail);
	    }

	    Map<String,Object> profile = naverOauthService.getUserInfo(accessToken);
	    Map<String, Object> userInfo = NaverProfileParser.toUserInfo(profile);

	    String id       = (String) userInfo.get("id");
	    String email    = (String) userInfo.get("email");
	    String nickname = (String) userInfo.get("nickname");

	    HashMap<String, Object> param = new HashMap<String, Object>();
	    param.put("providerId", id);

	    Map<String,Object> resBody = new HashMap<String,Object>();
	    resBody.put("ok", true);

	    UserAuth userAuth = userAuthService.selectOne(param);
	    if (userAuth != null) {
	        // 기존 회원 → 로그인 세션 심기
	        User user = userService.providerLogin(param);
	        securityLoginUtil.login(request, response, user);

	        resBody.put("next", "/"); // 로그인 후 갈 곳
	        return ResponseEntity.ok(resBody);

	    } else {
	        // 신규 → 가입 페이지로 안내, 세션에 정보 저장
	        request.getSession().setAttribute("providerId", id);
	        request.getSession().setAttribute("providerType", "naver");
	        request.getSession().setAttribute("email", email);
	        request.getSession().setAttribute("nickname", nickname);

	        resBody.put("next", "/main/nv_userJoin");
	        return ResponseEntity.ok(resBody);
	    }
	}

	
	@RequestMapping(value = "/kakaoLogin", method= {RequestMethod.GET, RequestMethod.POST})
    public String kakaoLogin(Model model,HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam String code) throws IOException{
		if (code == null) {
	        return "redirect:/";
	    }
		// 1. 인가 코드 받기 (@RequestParam String code)

        // 2. 토큰 받기
        String accessToken = kakaoApi.getAccessToken(code);
        
        if(accessToken==null) {
//        	ra.addFlashAttribute("toast", "카카오 인증 실패. 다시 시도해주세요.");
            return "redirect:/";
        }

        // 3. 사용자 정보 받기
        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        String id = (String)userInfo.get("id");
        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");

        System.out.println("id = " + id);
        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);
        
        HashMap<String, Object> param = new HashMap<>();
        param.put("providerId", id);
//        param.put("providerType", "kakao");
//        param.put("providerName", nickname); 
//        userAuthService.upsert(param);
        
        
        UserAuth userAuth = userAuthService.selectOne(param);
        if(userAuth!=null) {
        	//기존 회원 → 로그인
        	User user = userService.providerLogin(param);
        	securityLoginUtil.login(request, response, user);
        	return "yoim/main/nv_main";
        }else {
        	//가입을 할때 폰번호 받고 그거 인증 하나만 쓰기로하기
        	//이미 가입된아디있으면 가입한 아이디 알려주기 ㅇㅇ
        	//그리고 무조건 이메일로 가입 하게하기 그걸로 로그인 하라하고
        	//카카오나 네이버는 이메일 안받아도됨 아이디가 provider_id 이거임 ㅇㅇ
        	
        	//신규 → 가입 페이지로
        	request.getSession().setAttribute("providerId", id);
        	request.getSession().setAttribute("providerNickNm", nickname);
        	request.getSession().setAttribute("providerType", "kakao");
        	return "yoim/main/nv_userJoin";
        }
    }
	
}
