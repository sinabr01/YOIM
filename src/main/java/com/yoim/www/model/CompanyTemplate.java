package com.yoim.www.model;

import lombok.Data;

@Data
public class CompanyTemplate {
	private Long templateId;
	private Long companyId;
	private String title;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
