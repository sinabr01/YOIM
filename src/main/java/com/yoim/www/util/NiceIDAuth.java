package com.yoim.www.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import NiceID.Check.CPClient;

public class NiceIDAuth {

    private static String sSiteCode = "CE722"; // NICE로부터 부여받은 사이트 코드
    private static String sSitePassword = "mnTJiOU7pv9K"; // NICE로부터 부여받은 사이트 패스워드

    /** NiceID 인증 요청 (휴대폰 인증요청 페이지에서 필수 호출)
     * @param request (returnURL) 다음 페이지로 이동할 URL (http://naver.com)
     *
     * */
    public static Map<String, Object> main(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, Object> returnMap = new HashMap<String, Object>();


        CPClient niceCheck = new CPClient();

        String sRequestNumber = "REQ0000000001";        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String protocol = "https://";
        if (request.getRequestURL().toString().contains("http://"))
            protocol = "http://";
        String sReturnUrl = protocol + request.getServerName() +":"+ request.getServerPort() + "/main/nv_userJoin" ;      // 성공시 이동될 URL
        String sErrorUrl = protocol + request.getServerName() +":"+ request.getServerPort() + "/";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
        if( iReturn == 0 )
        {
            sEncData = niceCheck.getCipherData();
        }
        else if( iReturn == -1)
        {
            sMessage = "암호화 시스템 에러입니다.";
        }
        else if( iReturn == -2)
        {
            sMessage = "암호화 처리오류입니다.";
        }
        else if( iReturn == -3)
        {
            sMessage = "암호화 데이터 오류입니다.";
        }
        else if( iReturn == -9)
        {
            sMessage = "입력 데이터 오류입니다.";
        }
        else
        {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        returnMap.put("sEncData", sEncData);
        returnMap.put("iReturn", iReturn);
        returnMap.put("sMessage", sMessage);
        return returnMap;
    }


    /** 본인인증 성공 후 넘겨받은 데이터
     * @param request
     * */
    public static Map<String, Object> success(HttpServletRequest request) {
        HttpSession session = request.getSession();


        Map<String, Object> returnMap = new HashMap<String, Object>();
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sResponseNumber = "";		// 인증 고유번호
        String sAuthType = "";				// 인증 수단
        String sName = "";					// 성명
        String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
        String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
        String sBirthDate = "";				// 생년월일(YYYYMMDD)
        String sGender = "";				// 성별
        String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
        String sMobileNo = "";				// 휴대폰번호
        String sMobileCo = "";				// 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            HashMap mapresult = niceCheck.fnParse(sPlainData);


            sRequestNumber  = (String)mapresult.get("REQ_SEQ");
            sResponseNumber = (String)mapresult.get("RES_SEQ");
            sAuthType		= (String)mapresult.get("AUTH_TYPE");
            sName			= (String)mapresult.get("NAME");
            //sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate		= (String)mapresult.get("BIRTHDATE");
            sGender			= (String)mapresult.get("GENDER");
            sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
            sDupInfo		= (String)mapresult.get("DI");
            sConnInfo		= (String)mapresult.get("CI");
            sMobileNo		= (String)mapresult.get("MOBILE_NO");
            sMobileCo		= (String)mapresult.get("MOBILE_CO");

            //휴대폰 번호(sMobileNo) 파라미터 값을 임의의 번호로 변조 방지를 위해 세션에 저장
            session.setAttribute("sMobileNo",sMobileNo);
            session.setAttribute("sName",sName);
            session.setAttribute("sRequestNumber",sRequestNumber);
            session.setAttribute("sResponseNumber",sResponseNumber);
            session.setAttribute("sDupInfo",sDupInfo);
            session.setAttribute("sConnInfo",sConnInfo);

            String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
            if(!sRequestNumber.equals(session_sRequestNumber))
            {
                sMessage = "세션값 불일치 오류입니다.";
                sResponseNumber = "";
                sAuthType = "";
            }
        }
        else if( iReturn == -1)
        {
            sMessage = "복호화 시스템 오류입니다.";
        }
        else if( iReturn == -4)
        {
            sMessage = "복호화 처리 오류입니다.";
        }
        else if( iReturn == -5)
        {
            sMessage = "복호화 해쉬 오류입니다.";
        }
        else if( iReturn == -6)
        {
            sMessage = "복호화 데이터 오류입니다.";
        }
        else if( iReturn == -9)
        {
            sMessage = "입력 데이터 오류입니다.";
        }
        else if( iReturn == -12)
        {
            sMessage = "사이트 패스워드 오류입니다.";
        }
        else
        {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        returnMap.put("iReturn", iReturn);
        returnMap.put("sMessage", sMessage);


        returnMap.put("sRequestNumber", sRequestNumber);
        returnMap.put("sResponseNumber", sResponseNumber);
        returnMap.put("sAuthType", sAuthType);
        returnMap.put("sName", sName);
        returnMap.put("sBirthDate", sBirthDate);
        returnMap.put("sGender", sGender);
        returnMap.put("sNationalInfo", sNationalInfo);
        returnMap.put("sMobileNo", sMobileNo);
        returnMap.put("sMobileCo", sMobileCo);
        returnMap.put("sDupInfo", sDupInfo);
        returnMap.put("sConnInfo", sConnInfo);

        return returnMap;
    }

    public static String requestReplace(String paramValue, String gubun) {
        String result = "";

        if (paramValue != null) {

            paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            paramValue = paramValue.replaceAll("\\*", "");
            paramValue = paramValue.replaceAll("\\?", "");
            paramValue = paramValue.replaceAll("\\[", "");
            paramValue = paramValue.replaceAll("\\{", "");
            paramValue = paramValue.replaceAll("\\(", "");
            paramValue = paramValue.replaceAll("\\)", "");
            paramValue = paramValue.replaceAll("\\^", "");
            paramValue = paramValue.replaceAll("\\$", "");
            paramValue = paramValue.replaceAll("'", "");
            paramValue = paramValue.replaceAll("@", "");
            paramValue = paramValue.replaceAll("%", "");
            paramValue = paramValue.replaceAll(";", "");
            paramValue = paramValue.replaceAll(":", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll("#", "");
            paramValue = paramValue.replaceAll("--", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll(",", "");

            if(gubun != "encodeData"){
                paramValue = paramValue.replaceAll("\\+", "");
                paramValue = paramValue.replaceAll("/", "");
                paramValue = paramValue.replaceAll("=", "");
            }

            result = paramValue;

        }
        return result;
    }
}
