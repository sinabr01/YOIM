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

import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.KakaoApi;
import com.yoim.www.model.User;
import com.yoim.www.model.UserAuth;
import com.yoim.www.serviceImpl.NaverOauthService;
import com.yoim.www.serviceImpl.UserAuthService;
import com.yoim.www.serviceImpl.UserService;
import com.yoim.www.util.JwtTokenProvider;
import com.yoim.www.util.NaverProfileParser;
import com.yoim.www.util.SecurityLoginUtil;

import java.time.format.DateTimeFormatter;

@Controller
public class IndexController {
	
	@Autowired
	private KakaoApi kakaoApi;
	
	@Autowired
	UserAuthService userAuthService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	NaverOauthService naverOauthService;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	SecurityLoginUtil securityLoginUtil;
	
	NaverProfileParser naverProfileParser;
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	private CustomUserDetails convertUserToCustomUserDetails(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserId(user.getUserId());
        userDetails.setLoginId(user.getLoginId());
        userDetails.setUsername(user.getUserNm());
        userDetails.setNickname(user.getNickNm());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getLoginPw());
        userDetails.setUserType(user.getUserType());
        userDetails.setBirthDate(user.getBirthDate());
        userDetails.setGender(user.getGender());
        userDetails.setIntro(user.getIntro());
        userDetails.setInterests(user.getInterests());
        userDetails.setPhone(user.getPhone());
        userDetails.setProviderId(user.getProviderId());
        userDetails.setProviderType(user.getProviderType());
        userDetails.setUserDelYn(user.getUserDelYn());
        userDetails.setUserDelDate(user.getUserDelDate() != null ? user.getUserDelDate().format(formatter) : null);
        userDetails.setUserImgId(user.getUserImgId());
        userDetails.setRegistId(user.getRegistId());
        userDetails.setRegistDt(user.getRegistDt() != null ? user.getRegistDt().format(formatter) : null);
        userDetails.setUpdusrId(user.getUpdusrId());
        userDetails.setUpdtDt(user.getUpdtDt() != null ? user.getUpdtDt().format(formatter) : null);
        userDetails.setEnabled(!"Y".equals(user.getUserDelYn()));
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);
        return userDetails;
    }
	
	@GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
	public ResponseEntity<Void> quiet() {
	  return ResponseEntity.noContent().build(); // 204
	}
	
	@RequestMapping(value = "/fileTest", method = {RequestMethod.GET, RequestMethod.POST})
	public String fileTest(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
	    return "yoim/fileTest";
	}
	
	@RequestMapping(value = "/editorTest", method = {RequestMethod.GET, RequestMethod.POST})
	public String editorTest(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
	    return "yoim/editorTest";
	}
	
	
	@RequestMapping(value = "/test", method= {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model,HttpServletRequest request,
    		@AuthenticationPrincipal UserDetails user,
			HttpServletResponse response) throws IOException{
		
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
			Map<String, Object> fail = new HashMap<String, Object>();
			fail.put("ok", false);
			fail.put("message", "missing accessToken");
			return ResponseEntity.badRequest().body(fail);
		}

		Map<String, Object> profile = naverOauthService.getUserInfo(accessToken);
		Map<String, Object> userInfo = NaverProfileParser.toUserInfo(profile);

		String id = (String) userInfo.get("id");
		String email = (String) userInfo.get("email");
		String nickname = (String) userInfo.get("nickname");

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("providerId", id);

		Map<String, Object> resBody = new HashMap<String, Object>();
		resBody.put("ok", true);

		UserAuth userAuth = userAuthService.selectOne(param);
		if (userAuth != null) {
			// 기존 회원 → 로그인 세션 심기
			User user = userService.providerLogin(param);
			securityLoginUtil.login(request, response, user);

			resBody.put("next", "/"); // 로그인 후 갈 곳
			return ResponseEntity.ok(resBody);

		} else {
			// 신규 → 가입 정보 반환
			userInfo = new HashMap<>();
			userInfo.put("providerId", id);
			userInfo.put("providerType", "naver");
			userInfo.put("email", email);
			userInfo.put("nickname", nickname);

			resBody.put("next", "/main/nv_userJoin");
			resBody.put("userInfo", userInfo);
			return ResponseEntity.ok(resBody);
		}
	}

	@PostMapping("/kakaoLogin/token")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> kakaoLoginToken(
	        @RequestBody Map<String,String> body,
	        HttpServletRequest request,
	        HttpServletResponse response) {

	    String accessToken = body.get("accessToken");
	    if (accessToken == null || accessToken.isEmpty()) {
	        Map<String,Object> fail = new HashMap<String,Object>();
	        fail.put("ok", false);
	        fail.put("message", "missing accessToken");
	        return ResponseEntity.badRequest().body(fail);
	    }

	    Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);
	    String id = (String)userInfo.get("id");
	    String email = (String)userInfo.get("email");
	    String nickname = (String)userInfo.get("nickname");

	    HashMap<String, Object> param = new HashMap<String, Object>();
	    param.put("providerId", id);

	    Map<String,Object> resBody = new HashMap<String,Object>();
	    resBody.put("ok", true);

	    UserAuth userAuth = userAuthService.selectOne(param);
	    if (userAuth != null) {
	        // 기존 회원 → 로그인 세션 심기
	        User user = userService.providerLogin(param);
	        securityLoginUtil.login(request, response, user);

	        CustomUserDetails userDetails = convertUserToCustomUserDetails(user);
	        String token = tokenProvider.generateToken(userDetails);

	        resBody.put("token", token);
	        resBody.put("user", userDetails);
	        resBody.put("next", "/"); // 로그인 후 갈 곳
	        return ResponseEntity.ok(resBody);

	    } else {
	        // 신규 → 가입 정보 반환
	        Map<String, Object> userInfoMap = new HashMap<>();
	        userInfoMap.put("providerId", id);
	        userInfoMap.put("providerType", "kakao");
	        userInfoMap.put("email", email);
	        userInfoMap.put("nickname", nickname);

	        resBody.put("next", "/main/nv_userJoin");
	        resBody.put("userInfo", userInfoMap);
	        return ResponseEntity.ok(resBody);
	    }
	}
	@RequestMapping(value = "/kakaoLogin", method= {RequestMethod.GET, RequestMethod.POST})
    public String kakaoLogin(Model model,HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam String code) throws IOException{
		// 1. 인가 코드 받기 (@RequestParam String code)

        // 2. 토큰 받기
        String accessToken = kakaoApi.getAccessToken(code);

        if(accessToken==null) {
            return "redirect:/";
        }

        // 3. 사용자 정보 받기
        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        String id = (String)userInfo.get("id");
        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");

        HashMap<String, Object> param = new HashMap<>();
        param.put("providerId", id);


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
