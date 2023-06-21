package com.example.genie.domain.report.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportInfoObject {
    public final Long reportId;

    private final Long userId; //신고한 사람 아이디

    private final String contents;

    private final String userNickName;

    private final Type type;

    private final String imageUrl;

    @Builder
    public ReportInfoObject (Long reportId, Long userId, String contents, String userNickName, Integer type, String imageUrl) {
        this.reportId = reportId;
        this.userId = userId;
        this.contents = contents;
        this.userNickName = userNickName;
        this.type = Type.getType(type);
        this.imageUrl = imageUrl;
    }
}
