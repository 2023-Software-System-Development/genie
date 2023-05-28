package com.example.genie.domain.apply.entity;

import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "apply")
@Entity
@DynamicUpdate
public class Apply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pot_id")
    Pot pot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User applicant;

    @Builder
    public Apply(State state, Pot pot, User applicant) {
        this.state = state;
        this.pot = pot;
        this.applicant = applicant;
    }

}
