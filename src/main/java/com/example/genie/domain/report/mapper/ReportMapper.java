package com.example.genie.domain.report.mapper;



import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.model.ReportInfoObject;
import com.example.genie.domain.report.model.ReportObject;

public class ReportMapper {

    public static ReportObject toReportObject(Report report) {
        return ReportObject.builder()
                .reportId(report.getId())
                .contents(report.getContents())
                .build();
    }

    public static ReportInfoObject toReportInfoObject(Report report) {
        return ReportInfoObject.builder()
                .reportId(report.getId())
                .userId(report.getUserId())
                .contents(report.getContents())
                .userNickName(report.getUserNickName())
                .type(report.getType())
                .imageUrl(report.getImageUrl())
                .build();
    }
}
