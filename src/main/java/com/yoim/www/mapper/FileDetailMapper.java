package com.yoim.www.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yoim.www.model.FileDetail;

@Mapper
public interface FileDetailMapper {
    int insert(FileDetail fileDetail);
    List<FileDetail> findByUploadId(Long fileUploadId);
    FileDetail findById(Long fileId);
    int deleteById(Long fileId);
}