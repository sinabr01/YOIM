package com.yoim.www.model;

import lombok.Data;

@Data
public class User {
	private Long userSeq;
	private String userId;
	private String userPw;
	private String userNm;
	private String nickNm;
	private String email;
	private String birthDate;
	private String gender;
	private String intro;
	private String interests;
	private String phone;
	private String providerId;
	private String providerType;
	private String userDelYn;
	private String userDelDate;
	private String userImg;
	private String userType;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
