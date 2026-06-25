package com.example.genie.domain.report.mapper;



import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.dto.ReportInfoDto;
import com.example.genie.domain.report.dto.ReportDto;

public class ReportMapper {

    public static ReportDto toReportObject(Report report) {
        return ReportDto.builder()
                .reportId(report.getId())
                .contents(report.getContents())
                .build();
    }

    public static ReportInfoDto toReportInfoObject(Report report) {
        return ReportInfoDto.builder()
                .reportId(report.getId())
                .userId(report.getUserId())
                .contents(report.getContents())
                .userNickName(report.getUserNickName())
                .type(report.getType())
                .imageUrl(report.getImageUrl())
                .build();
    }
}
