package com.yoim.www.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yoim.www.model.ChatRoom;

@Mapper
public interface ChatRoomMapper {
	int insertRoom(Map<String,Object> p);
	Long lastInsertId();
	int incLastSeq(@Param("roomId") Long roomId);
	Integer getLastSeq(@Param("roomId") Long roomId);
	List<Map<String, Object>> listRoomsForUser(@Param("userId") Long userId);
}