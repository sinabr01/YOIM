package com.yoim.www.mapper.chat;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatDmMapper {
	Long findDmRoom(@Param("low") long low, @Param("high") long high);
	int insertDm(@Param("roomId") long roomId, @Param("low") long low, @Param("high") long high);
}