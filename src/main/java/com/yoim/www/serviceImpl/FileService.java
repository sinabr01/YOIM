package com.yoim.www.serviceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yoim.www.mapper.FileDetailMapper;
import com.yoim.www.mapper.FileUploadMapper;
import com.yoim.www.model.FileDetail;
import com.yoim.www.model.FileUpload;

@Service
public class FileService{

	@Autowired
	FileUploadMapper uploadMapper;

	@Autowired
	FileDetailMapper detailMapper;
	
	@Value("${yoim.upload.base-dir}")
    private String baseDir;

	@Transactional
    public Long createUploadGroup(String uploadCode, String userId) {
        FileUpload dto = new FileUpload();
        dto.setUploadCode(uploadCode);
        dto.setStatus("TEMP");
        dto.setRegistId(userId);
        uploadMapper.insert(dto);
        System.out.println(dto.getFileUploadId());
        return dto.getFileUploadId();
    }
	
	@Transactional
    public List<FileDetail> saveFiles(Long uploadId,
                                         List<MultipartFile> files,
                                         Integer isMain,
                                         Integer sortOrder,
                                         String userId) throws IOException {
        if (uploadId == null) {
            throw new IllegalArgumentException("uploadId is null");
        }
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<FileDetail> out = new ArrayList<>();
        int idx = 0;
        for (MultipartFile mf : files) {
            int main = (idx == 0 ? (isMain != null ? isMain : 1) : 0);
            Integer order = (sortOrder != null ? sortOrder : 0);
            FileDetail saved = storeFile(uploadId, mf, userId, main, order);
            out.add(FileDetail.from(saved));
            idx++;
        }
        return out;
    }

    @Transactional
    public FileDetail storeFile(Long uploadId, MultipartFile mf, String userId, Integer isMain, Integer sortOrder) throws IOException {
        if (mf == null || mf.isEmpty()) throw new IllegalArgumentException("empty file");

        // 경로/파일명 생성
        LocalDate today = LocalDate.now();
        String sub = String.format("/%04d/%02d/%02d", today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String org = mf.getOriginalFilename() == null ? "file" : mf.getOriginalFilename();
        String ext = org.contains(".") ? org.substring(org.lastIndexOf('.') + 1).toLowerCase() : "";
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String stored = uuid + (ext.isEmpty() ? "" : "." + ext);

        // 디스크 저장
        Path dir = Paths.get(baseDir + sub);
        Files.createDirectories(dir);
        Path target = dir.resolve(stored);
        try (InputStream in = mf.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // DB 저장
        FileDetail d = new FileDetail();
        d.setFileUploadId(uploadId);
        d.setFileStreCours(sub);
        d.setStreFileNm(stored);
        d.setOrginlFileNm(org);
        d.setFileExtsn(ext);
        d.setFileSize(mf.getSize());
        d.setIsMain(isMain != null ? isMain : 0);
        d.setSortOrder(sortOrder);
        d.setRegistId(userId);
        detailMapper.insert(d);
        return d;
    }

    @Transactional
    public void finalizeUpload(Long uploadId, String userId) {
    	FileUpload dto = new FileUpload();
        dto.setFileUploadId(uploadId);
        dto.setStatus("FINAL");
        dto.setUpdusrId(userId);
        uploadMapper.updateStatus(dto);
    }

    public Resource loadAsResource(Long fileId) throws IOException {
    	FileDetail d = detailMapper.findById(fileId);
        if (d == null) throw new FileNotFoundException("no file");
        Path p = Paths.get(baseDir + d.getFileStreCours()).resolve(d.getStreFileNm());
        return new UrlResource(p.toUri());
    }

    @Transactional
    public void deleteFile(Long fileId) {
        detailMapper.deleteById(fileId); // (디스크 삭제는 선택)
    }

    public List<FileDetail> listByUpload(Long uploadId) {
        return detailMapper.findByUploadId(uploadId);
    }

}
