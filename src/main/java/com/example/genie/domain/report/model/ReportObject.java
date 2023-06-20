package com.example.genie.domain.report.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportObject {

    public final Long reportId;
    private final String contents;

    @Builder
    public ReportObject (Long reportId, String contents) {
        this.reportId = reportId;
        this.contents = contents;
    }
}
