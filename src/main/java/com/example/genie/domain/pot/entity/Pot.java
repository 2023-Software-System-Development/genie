package com.example.genie.domain.pot.entity;

import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.pot.form.PotCreateForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.genie.domain.pot.entity.State.RECRUITING;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@Table(name = "pot")
@Entity
@DynamicUpdate
public class Pot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pot_id")
    private Long id;

    private String potName;

    private String ottType;

    private Integer price;

    private Integer recruit;

    private Integer term;

    private String ott_id;

    private String ott_pwd;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long master_id;

    @Enumerated(EnumType.STRING)
    private State state;


    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Apply> applies = new ArrayList<>();

    //==생성 메서드==//
    public static Pot createPot(PotCreateForm potCreateForm, Long userId) {
        Pot pot = Pot.builder()
                .potName(potCreateForm.getPotName())
                .ottType(potCreateForm.getOttType())
                .price(potCreateForm.getPrice())
                .recruit(potCreateForm.getRecruit())
                .term(potCreateForm.getTerm())
                .master_id(userId)
                .state(RECRUITING)
                .build();

        return pot;
    }
}
