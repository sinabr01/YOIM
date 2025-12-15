package com.yoim.www.model.chat;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMember {
	private Long roomId;
	private Long userId;
	private Long role;
	private LocalDateTime joinedAt;
	private LocalDateTime leftAt;
	private Long lastReadSeq;
	private Long notificationsOn;
	private LocalDateTime mutedUntil;
}
