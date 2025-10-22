package com.yoim.www.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMemberMapper {
	int insertMember(@Param("roomId") long roomId, @Param("userId") long userId, @Param("role") int role);
	int updateLastReadSeq(@Param("roomId") long roomId, @Param("userId") long userId, @Param("seq") int seq);
	Integer readCountForMessage(@Param("roomId") long roomId, @Param("seq") int seq,
			@Param("createdAt") String createdAt, @Param("senderId") Long senderId);
}