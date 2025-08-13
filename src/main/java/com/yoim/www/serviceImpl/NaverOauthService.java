package com.yoim.www.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverOauthService {
	
    @Value("${naver.client-id}") String clientId;
    @Value("${naver.client-secret}") String clientSecret;

    public String exchangeCodeForToken(String code, String state) {
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", state);

        ResponseEntity<Map> res = rt.postForEntity(
            "https://nid.naver.com/oauth2.0/token",
            new HttpEntity<>(params, new HttpHeaders(){{
                setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            }}),
            Map.class
        );
        return (String) res.getBody().get("access_token");
    }

    public Map<String,Object> getUserInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);
        ResponseEntity<Map> res = rt.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.GET,
            new HttpEntity<>(h),
            Map.class
        );
        return res.getBody(); // {"resultcode":"00","message":"success","response":{...}}
    }
}
