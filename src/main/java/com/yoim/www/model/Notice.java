package com.yoim.www.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class Notice{
	private Long noticeId;
	private String title;
	private String content;
	private String pinnedYn;
	private String useYn;
	private String registId;
	private String registDt;
	private String updusrId;
	private String updtDt;
}
