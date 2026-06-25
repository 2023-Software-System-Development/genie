package com.example.genie.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportDto {

    public final Long reportId;
    private final String contents;

    @Builder
    public ReportDto (Long reportId, String contents) {
        this.reportId = reportId;
        this.contents = contents;
    }
}
