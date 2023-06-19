package com.example.genie.domain.report.service;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    private static String uploadDir = System.getProperty("user.home") + File.separator + "GenieFile";
    public String fileUpload(MultipartFile multipartFile) {
        // 파일 이름으로 쓸 uuid 생성
        String originalName = multipartFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        String uuid = UUID.randomUUID().toString();
        String saveFileName = uploadDir + File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(saveFileName);
        try {
            multipartFile.transferTo(savePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return saveFileName;

    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
