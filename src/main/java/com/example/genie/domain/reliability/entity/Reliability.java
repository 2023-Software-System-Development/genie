package com.example.genie.domain.reliability.entity;


import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.user.entity.User;
import lombok.Builder;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String history;
    private Integer score;

    @Builder
    public Reliability(User user, String history, Integer score) {
        this.user = user;
        this.history = history;
        this.score = score;
    }
}
