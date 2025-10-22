package com.yoim.www.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatRoom {
	private Long roomId;
	private Long roomType;
	private String roomName;
	private Long createdBy;
	private Long lastSeq;
	private LocalDateTime lastMsgAt;
	private LocalDateTime registDt;
	private LocalDateTime updtDt;
}
