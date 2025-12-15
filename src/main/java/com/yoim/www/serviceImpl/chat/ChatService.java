package com.yoim.www.serviceImpl.chat;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yoim.www.mapper.chat.ChatDmMapper;
import com.yoim.www.mapper.chat.ChatMemberMapper;
import com.yoim.www.mapper.chat.ChatMsgMapper;
import com.yoim.www.mapper.chat.ChatRoomMapper;
import com.yoim.www.model.chat.ChatMsg;
import com.yoim.www.model.MessageDto;
import com.yoim.www.model.MessagePayload;

@Service
public class ChatService {
	
	@Autowired
	ChatRoomMapper roomM;
	
	@Autowired
	ChatDmMapper dmM;
	
	@Autowired
	ChatMemberMapper memM;
	
	@Autowired
	ChatMsgMapper msgM;

	@Transactional
	public long createDm(long a, long b, long createdBy) {
		long low = Math.min(a, b), high = Math.max(a, b);
		Long roomId = dmM.findDmRoom(low, high);
		if (roomId != null)
			return roomId;
		
		HashMap<String, Object> p = new HashMap<>();
		p.put("roomType", 1);
    	p.put("roomName", null);
    	p.put("createdBy", 7);
    	roomM.insertRoom(p);
		long newRoomId = roomM.lastInsertId();
		memM.insertMember(newRoomId, a, 1);
		memM.insertMember(newRoomId, b, 3);
		dmM.insertDm(newRoomId, low, high); // 경합 시 중복키 → 기존 방 사용
		return newRoomId;
	}

	@Transactional
	public MessageDto send(MessagePayload p) {
		roomM.incLastSeq(p.getRoomId());
		int seq = roomM.getLastSeq(p.getRoomId());
		ChatMsg e = ChatMsg.of(p.getRoomId(), p.getSenderId(), p.getMsgType(), p.getContent(), p.getFileUploadId(),
				p.getReplyToMsgId(), seq);
		msgM.insertMessage(e);
		// 읽음 카운트(보낸 직후 0일 가능) → UI에선 브로드캐스트 후 클라들이 read 업데이트
		return new MessageDto(e.getMsgId(), e.getRoomId(), e.getSenderId(), e.getMsgType(), e.getContent(),
				(long) e.getSeq(), e.getRegistDt(), 0);
	}

	@Transactional
	public void markRead(long roomId, long userId, int lastSeq) {
		memM.updateLastReadSeq(roomId, userId, lastSeq);
	}
}
