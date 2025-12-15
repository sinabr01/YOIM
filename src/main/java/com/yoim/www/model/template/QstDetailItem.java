package com.yoim.www.model.template;

import lombok.Data;

@Data
public class QstDetailItem {
	private Long qstItemId;
	private Long qstId;
	private String itemName;
	private int orderNo;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
