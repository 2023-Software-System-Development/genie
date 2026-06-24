package com.example.genie.domain.report.service;


import com.example.genie.common.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class FileService {

    private static final Path UPLOAD_DIR =
            Paths.get(System.getProperty("user.home"), "GenieFile").toAbsolutePath().normalize();
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_EXT =
            new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "webp"));

    public String fileUpload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new CustomException("업로드할 파일이 없습니다.");
        }
        if (multipartFile.getSize() > MAX_SIZE) {
            throw new CustomException("파일 크기는 5MB를 넘을 수 없습니다.");
        }

        // 원본 파일명은 신뢰하지 않고 확장자 검증에만 사용 (경로 조작 방지)
        String originalName = StringUtils.cleanPath(
                multipartFile.getOriginalFilename() == null ? "" : multipartFile.getOriginalFilename());
        String ext = extractExt(originalName).toLowerCase();
        if (!ALLOWED_EXT.contains(ext)) {
            throw new CustomException("이미지 파일(jpg, jpeg, png, gif, webp)만 업로드할 수 있습니다.");
        }
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException("이미지 파일만 업로드할 수 있습니다.");
        }

        // 저장 파일명은 UUID로만 생성해 원본 파일명의 경로 요소를 완전히 배제
        String saveFileName = UUID.randomUUID().toString() + "." + ext;
        Path target = UPLOAD_DIR.resolve(saveFileName).normalize();
        if (!target.startsWith(UPLOAD_DIR)) {
            throw new CustomException("잘못된 파일 경로입니다.");
        }

        try {
            Files.createDirectories(UPLOAD_DIR);
            multipartFile.transferTo(target);
        } catch (IOException e) {
            throw new CustomException("파일 업로드에 실패했습니다.");
        }

        // 절대 경로가 아닌 저장 파일명만 반환/저장 (서버 경로 노출 방지)
        return saveFileName;
    }

    private String extractExt(String filename) {
        int pos = filename.lastIndexOf('.');
        return pos == -1 ? "" : filename.substring(pos + 1);
    }

}
