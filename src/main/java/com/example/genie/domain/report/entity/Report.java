package com.example.genie.domain.report.entity;


import com.example.genie.common.BaseEntity;
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
    private Long userNickName;

    private String type;
    private String contents;
    private String imageUrl;
    private String user_id;
}
