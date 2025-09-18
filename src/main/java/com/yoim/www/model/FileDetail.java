package com.yoim.www.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FileDetail {
	private Long fileId;          // BIGINT → Long
	private Long fileUploadId;
	private String fileStreCours;
	private String streFileNm;
	private String orginlFileNm;
	private String fileExtsn;
	private Long fileSize;
	private int isMain;
	private int sortOrder;
	private String registId;          // VARCHAR
	private LocalDateTime registDt;   // TIMESTAMP → LocalDateTime
	private String updusrId;          // VARCHAR
	private LocalDateTime updtDt;     // TIMESTAMP → LocalDateTime

	
	public static FileDetail from(FileDetail e) {
        FileDetail d = new FileDetail();
        d.setFileId(e.getFileId());
        d.setFileUploadId(e.getFileUploadId());
        d.setFileStreCours(e.getFileStreCours());
        d.setStreFileNm(e.getStreFileNm());
        d.setOrginlFileNm(e.getOrginlFileNm());
        d.setFileExtsn(e.getFileExtsn());
        d.setFileSize(e.getFileSize());
        d.setIsMain(e.getIsMain());
        d.setSortOrder(e.getSortOrder());
        return d;
    }
}
