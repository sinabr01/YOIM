package com.yoim.www.model;

import lombok.Data;

@Data
public class UserAuth {
	private Long userAuthId;
	private String providerId;
	private String providerType;
	private String providerName;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
