package com.yoim.www.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Data
@Slf4j
public class KakaoApi {

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    private static final String TOKEN_URL     = "https://kauth.kakao.com/oauth/token";
    private static final String USERINFO_URL  = "https://kapi.kakao.com/v2/user/me";
    private static final String LOGOUT_URL    = "https://kapi.kakao.com/v1/user/logout";
    private static final int    CONN_TIMEOUT  = 8000;
    private static final int    READ_TIMEOUT  = 8000;

    /** access_token 없으면 null 반환(컨트롤러에서 redirect:/ 처리 용이) */
    public String getAccessToken(String code) {
        String result = null;
        try {
            String body = "grant_type=authorization_code"
                    + "&client_id="   + enc(kakaoApiKey)
                    + "&redirect_uri=" + enc(kakaoRedirectUri)
                    + "&code="        + enc(code);

            HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONN_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8))) {
                bw.write(body);
            }

            int responseCode = conn.getResponseCode();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8))) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                String json = sb.toString();

                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

                // 정상 토큰
                if (obj.has("access_token") && !obj.get("access_token").isJsonNull()) {
                    String accessToken = obj.get("access_token").getAsString();
                    // refresh_token 필요하면 아래 주석 해제
                    // String refreshToken = obj.has("refresh_token") && !obj.get("refresh_token").isJsonNull()
                    //        ? obj.get("refresh_token").getAsString() : null;
                    return accessToken;
                }

                // 에러 로그 남기고 null
                String err  = obj.has("error") ? obj.get("error").getAsString() : ("http_" + responseCode);
                String desc = obj.has("error_description") && !obj.get("error_description").isJsonNull()
                        ? obj.get("error_description").getAsString() : json;
                log.warn("[KakaoApi] token error: {} - {}", err, desc);
                return null;
            }
        } catch (Exception e) {
            log.warn("[KakaoApi] getAccessToken exception", e);
            return null;
        }
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(USERINFO_URL).openConnection();
            conn.setRequestMethod("POST"); // GET도 가능
            conn.setConnectTimeout(CONN_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8))) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                String json = sb.toString();

                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                if (obj.has("id") && !obj.get("id").isJsonNull()) {
                    String id = obj.get("id").getAsString();

                    JsonObject properties   = obj.has("properties")     && obj.get("properties").isJsonObject()
                            ? obj.get("properties").getAsJsonObject() : new JsonObject();
                    
                    System.out.println(properties.toString());
                    
                    JsonObject kakaoAccount = obj.has("kakao_account")  && obj.get("kakao_account").isJsonObject()
                            ? obj.get("kakao_account").getAsJsonObject() : new JsonObject();

                    String nickname = properties.has("nickname") && !properties.get("nickname").isJsonNull()
                            ? properties.get("nickname").getAsString() : "";
                    String email = kakaoAccount.has("email") && !kakaoAccount.get("email").isJsonNull()
                            ? kakaoAccount.get("email").getAsString() : "";

                    userInfo.put("id", id);
                    userInfo.put("nickname", nickname);
                    userInfo.put("email", email);
                } else {
                    log.warn("[KakaoApi] userinfo error body: {}", json);
                }
            }
        } catch (Exception e) {
            log.warn("[KakaoApi] getUserInfo exception", e);
        }
        return userInfo;
    }

    public void kakaoLogout(String accessToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(LOGOUT_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONN_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                String json = sb.toString();
                log.debug("[KakaoApi] logout response: {}", json);
            }
        } catch (Exception e) {
            log.warn("[KakaoApi] kakaoLogout exception", e);
        }
    }

    private static String enc(String s) {
        if (s == null) return "";
        try {
            return URLEncoder.encode(s, "UTF-8"); // JDK8 호환
        } catch (UnsupportedEncodingException e) {
            // UTF-8은 항상 지원되지만, 시그니처상 체크 예외 필요
            return s;
        }
    }
}
