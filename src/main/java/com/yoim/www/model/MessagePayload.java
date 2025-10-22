package com.yoim.www.model;

import lombok.Data;

@Data
public class MessagePayload {

    // 클라 -> 서버 전송용
    private Long roomId;
    private Long senderId;
    private Integer msgType;
    private String content;
    private Long fileUploadId;
    private Long replyToMsgId;

    public MessagePayload(Long roomId, Long senderId, Integer msgType, String content,
                          Long fileUploadId, Long replyToMsgId) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.msgType = msgType;
        this.content = content;
        this.fileUploadId = fileUploadId;
        this.replyToMsgId = replyToMsgId;
    }

}

