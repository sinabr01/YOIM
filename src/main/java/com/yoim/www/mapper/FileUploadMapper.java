package com.yoim.www.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yoim.www.model.FileUpload;

@Mapper
public interface FileUploadMapper {
    int insert(FileUpload fileUpload);
    int updateStatus(FileUpload fileUpload);
    FileUpload findById(Long fileUploadId);
}