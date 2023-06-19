package com.example.genie.domain.report.entity;


import com.example.genie.common.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "report")
@Entity
@DynamicUpdate
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    private String userNickName;
    private int type;
    private String contents;
    private String imageUrl;
    private Long userId;

    @Builder
    public Report(String userNickName, int type, String contents, String imageUrl, Long userId) {
        this.userNickName = userNickName;
        this.type = type;
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }
}
