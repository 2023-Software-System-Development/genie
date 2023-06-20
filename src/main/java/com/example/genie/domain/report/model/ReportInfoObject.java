package com.example.genie.domain.report.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ReportInfoObject {
    public final Long reportId;
    private final String contents;

    private final String userNickName;

    private final Integer type;

    private final String imageUrl;

    @Builder
    public ReportInfoObject (Long reportId, String contents, String userNickName, Integer type, String imageUrl) {
        this.reportId = reportId;
        this.contents = contents;
        this.userNickName = userNickName;
        this.type = type;
        this.imageUrl = imageUrl;
    }
}
