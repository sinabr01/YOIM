package com.yoim.www.mapper.chat;

import com.yoim.www.model.chat.ChatMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMemberMapper {
	int insertMember(@Param("roomId") long roomId, @Param("userId") long userId, @Param("role") int role);
	int updateLastReadSeq(@Param("roomId") long roomId, @Param("userId") long userId, @Param("seq") int seq);
	Integer readCountForMessage(@Param("roomId") long roomId, @Param("seq") int seq,
			@Param("createdAt") String createdAt, @Param("senderId") Long senderId);
	List<ChatMember> selectMemberReadStates(@Param("roomId") long roomId);
}