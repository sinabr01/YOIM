package com.yoim.www.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDto {
    private Long msgId;
    private Long roomId;
    private Long senderId;
    private Integer msgType;
    private String content;
    private Long seq;        // 방 내 일련번호
    private LocalDateTime registDt;    // 필요시 LocalDateTime으로 교체 가능
    private Integer readCount;  // 읽음 인원 수

    public MessageDto() {}

    public MessageDto(Long msgId, Long roomId, Long senderId, Integer msgType, String content,
                      long seq, LocalDateTime registDt, Integer readCount) {
        this.msgId = msgId;
        this.roomId = roomId;
        this.senderId = senderId;
        this.msgType = msgType;
        this.content = content;
        this.seq = seq;
        this.registDt = registDt;
        this.readCount = readCount;
    }

}

