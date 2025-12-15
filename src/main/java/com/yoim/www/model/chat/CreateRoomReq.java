package com.yoim.www.model.chat;

import java.util.List;

import lombok.Data;

@Data
public class CreateRoomReq {
	private Integer type;
    private String roomName;
    private List<Long> members;
    private Long me;
}
