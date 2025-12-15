package com.yoim.www.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.yoim.www.model.MessagePayload;
import com.yoim.www.serviceImpl.chat.ChatService;

import lombok.var;

@Controller
class ChatStompController {

    @Autowired
    private ChatService svc;

    @Autowired
    private SimpMessagingTemplate broker;

    @MessageMapping("/rooms/{roomId}/send")
    public void send(@DestinationVariable long roomId, MessagePayload payload) {
        var dto = svc.send(payload);
        broker.convertAndSend("/topic/rooms/" + roomId, dto);
    }
}
