package com.yoim.www.model.chat;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsg {
    private Long msgId;
    private Long roomId;
    private Long senderId;
    private Integer msgType;          // 1=TEXT,2=IMAGE,3=FILE,4=SYSTEM
    private String content;
    private Long fileUploadId;
    private Long replyToMsgId;
    private Integer seq;              // 방 내 일련번호
    private LocalDateTime registDt;   // DB DEFAULT
    private LocalDateTime updtDt;
    private LocalDateTime delDt;

    public static ChatMsg of(Long roomId, Long senderId, Integer msgType, String content,
                             Long fileUploadId, Long replyToMsgId, int seq) {
        ChatMsg m = new ChatMsg();
        m.roomId = roomId;
        m.senderId = senderId;
        m.msgType = msgType;
        m.content = content;
        m.fileUploadId = fileUploadId;
        m.replyToMsgId = replyToMsgId;
        m.seq = seq;
        return m;
    }
}
