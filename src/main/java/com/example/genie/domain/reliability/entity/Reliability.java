package com.example.genie.domain.reliability.entity;


import com.example.genie.common.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "reliability")
@Entity
@DynamicUpdate
public class Reliability extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reliability_id")
    private Long id;

    private String history;
    private Integer score;
}
