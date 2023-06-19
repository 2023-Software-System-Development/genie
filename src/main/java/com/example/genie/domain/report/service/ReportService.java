package com.example.genie.domain.report.service;

import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.form.ReportForm;
import com.example.genie.domain.report.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Service
public class ReportService {
    final private ReportRepository reportRepository;
    final private FileService fileService;
    public Report createReport(Long userId, ReportForm reportForm) {
        String fileName=null;
        if (reportForm.getImage() != null && !reportForm.getImage().isEmpty())
            fileName = fileService.fileUpload(reportForm.getImage());
        Report report = Report.builder()
                .userId(userId)
                .type(reportForm.getType())
                .userNickName(reportForm.getUserNickName())
                .imageUrl(fileName)
                .contents(reportForm.getContents())
                .build();


        return reportRepository.save(report);

    }


}
