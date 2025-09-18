package com.yoim.www.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FileUpload {
	private Long fileUploadId;
	private String uploadCode;
	private String status;
	private String registId;          // VARCHAR
	private LocalDateTime registDt;   // TIMESTAMP → LocalDateTime
	private String updusrId;          // VARCHAR
	private LocalDateTime updtDt;     // TIMESTAMP → LocalDateTime

}
