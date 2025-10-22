package com.yoim.www.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yoim.www.mapper.ChatMsgMapper;
import com.yoim.www.mapper.ChatRoomMapper;
import com.yoim.www.model.ChatRoom;
import com.yoim.www.model.CreateRoomReq;
import com.yoim.www.model.CustomUserDetails;
import com.yoim.www.model.MessageDto;
import com.yoim.www.model.ReadReq;
import com.yoim.www.serviceImpl.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
	
	@Autowired
    private ChatService svc;

    @Autowired
    private ChatRoomMapper roomM;

    @Autowired
    private ChatMsgMapper msgM;


    @PostMapping("/rooms")
    public Map<String, Object> createRoom(@RequestBody CreateRoomReq req,Authentication authentication) {
    	CustomUserDetails user = (CustomUserDetails) authentication.getDetails();
    	long roomId;
        if (Integer.valueOf(1).equals(req.getType()) && req.getMembers() != null && req.getMembers().size() == 2) {
            roomId = svc.createDm(req.getMembers().get(0), req.getMembers().get(1), req.getMe());
        } else {
            // 그룹방 간단 생성
        	
        	Map<String,Object> p = new HashMap<>();
        	p.put("roomType", 1);
        	p.put("roomName", null);
        	p.put("createdBy", user.getUserId());
        	roomM.insertRoom(p);
        	
        	roomId = ((Number)p.get("roomId")).longValue();
        }
        Map<String,Object> res = new HashMap<>();
        res.put("roomId", roomId);
        return res;
    }

    @GetMapping("/rooms")
    public List<Map<String, Object>> myRooms(@RequestParam long userId) {
        return roomM.listRoomsForUser(userId);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public List<Map<String, Object>> messages(@PathVariable long roomId,
                                            @RequestParam(required = false) Integer beforeSeq,
                                            @RequestParam(defaultValue = "50") int limit) {
        return msgM.recentMessages(roomId, beforeSeq, limit);
    }

    @PostMapping("/rooms/{roomId}/read")
    public void read(@PathVariable long roomId, @RequestBody ReadReq req) {
        svc.markRead(roomId, req.getUserId(), req.getLastSeq());
    }
}
