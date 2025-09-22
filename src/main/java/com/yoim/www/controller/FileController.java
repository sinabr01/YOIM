package com.yoim.www.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yoim.www.model.FileDetail;
import com.yoim.www.serviceImpl.FileService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

	@Autowired
	FileService fileService;

	/** 업로드 그룹 생성 */
    @PostMapping(
        value = "/uploads",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> createUpload(
            @RequestParam String uploadCode,
            @RequestParam(required = false) String userId) {
        String who = (userId != null && !userId.trim().isEmpty()) ? userId : "system";
        Long id = fileService.createUploadGroup(uploadCode, who);
        Map<String, Object> body = new HashMap<>();
        body.put("fileUploadId", id);
        return ResponseEntity.ok(body);
    }

    /** 파일 업로드 
     * @throws IOException */
    @PostMapping(
        value = "/uploads/{fileUploadId}/files",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<FileDetail>> uploadFiles(
            @PathVariable Long fileUploadId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(required = false, defaultValue = "0") int isMain,
            @RequestParam(required = false, defaultValue = "0") Integer sortOrder,
            @RequestParam(required = false) String userId) throws IOException {
    	String who = (userId != null && !userId.trim().isEmpty()) ? userId : "system";
        List<FileDetail> saved = fileService.saveFiles(fileUploadId, files, isMain, sortOrder, who);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/uploads/{uploadId}/finalize")
    public ResponseEntity<Map<String, Object>> finalize(@PathVariable Long uploadId, @RequestParam(required=false) String userId) {
    	Map<String,Object> body = new HashMap<>();
    	body.put("fileUploadId", uploadId);
        body.put("status", "FINAL");
    	fileService.finalizeUpload(uploadId, nvl(userId));
    	return ResponseEntity.ok(body);
    }

    @GetMapping("/uploads/{uploadId}")
    public List<FileDetail> list(@PathVariable Long uploadId) {
        return fileService.listByUpload(uploadId);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) throws IOException {
        Resource res = fileService.loadAsResource(fileId);
        // 파일명 헤더(간단 버전)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(res);
    }

    private String nvl(String v) { return v == null ? "system" : v; }
}
