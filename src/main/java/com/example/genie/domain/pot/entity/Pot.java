package com.example.genie.domain.pot.entity;

import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Integer remain;

    private String ott_id;

    private String ott_pwd;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User master;

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Apply> applies = new ArrayList<>();

    @Builder
    public Pot(String potName, String ottType, Integer price, Integer recruit, Integer term, Integer remain, String ott_id,
               String ott_pwd, LocalDateTime startDate, LocalDateTime endDate, User master, State state) {
        this.potName = potName;
        this.ottType = ottType;
        this.price = price;
        this.recruit = recruit;
        this.term = term;
        this.remain = remain;
        this.ott_id = ott_id;
        this.ott_pwd = ott_pwd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.master = master;
        this.state = state;
    }
}
