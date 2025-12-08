package com.yoim.www.model;

import lombok.Data;

@Data
public class QstDetail {
	private Long qstId;
	private Long templateQstId;
	private String qstOption;
	private String qstName;
	private int orderNo;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
