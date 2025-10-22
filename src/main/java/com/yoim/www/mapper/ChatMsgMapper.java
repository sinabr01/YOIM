package com.yoim.www.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yoim.www.model.ChatMsg;

@Mapper
public interface ChatMsgMapper {
	int insertMessage(ChatMsg charMsg);
	List<Map<String,Object>> recentMessages(@Param("roomId") long roomId, @Param("beforeSeq") Integer beforeSeq, @Param("limit") int limit);
}