package com.yoim.www.model;

import lombok.Data;

@Data
public class TemplateQst {
	private Long templateQstId;
	private Long templateId;
	private String question;
	private String requiredYn;
	private String type;
	private int orderNo;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
