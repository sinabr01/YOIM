package com.yoim.www.util;

import java.util.HashMap;
import java.util.Map;

public final class NaverProfileParser {
    private NaverProfileParser() {}

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toUserInfo(Map<String, Object> profile) {
        Map<String, Object> out = new HashMap<String, Object>();
        if (profile == null) return out;

        Object respObj = profile.get("response");
        if (!(respObj instanceof Map)) return out;

        Map<String, Object> resp = (Map<String, Object>) respObj;

        String id        = (String) resp.get("id");
        String email     = (String) resp.get("email");         // 선택동의일 수 있음
        String nickname  = (String) resp.get("nickname");      // 선택동의일 수 있음
        String mobile    = (String) resp.get("mobile");        // 010-XXXX-XXXX
        String mobileE164= (String) resp.get("mobile_e164");   // +8210XXXXXXXX

        out.put("id", id);
        out.put("email", email != null ? email : "");
        out.put("nickname", nickname != null ? nickname : "");
        out.put("phone", mobile != null ? mobile : (mobileE164 != null ? mobileE164 : ""));
        // 추가로 쓰고 싶으면 더 담기
        out.put("name", (String) resp.get("name"));
        out.put("birthyear", (String) resp.get("birthyear"));
        out.put("birthday", (String) resp.get("birthday"));
        out.put("gender", (String) resp.get("gender"));
        out.put("profile_image", (String) resp.get("profile_image"));

        return out;
    }
}

