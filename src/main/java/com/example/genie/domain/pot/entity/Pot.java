package com.example.genie.domain.pot.entity;

import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.pot.form.PotEditOngoingForm;
import com.example.genie.domain.pot.form.PotEditRecruitingForm;
import com.example.genie.domain.pot.form.PotStartForm;
import com.example.genie.domain.pot.model.PotInfoObject;
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

    private String ottId;

    private String ottPw;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User master;

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Interest> interests = new ArrayList<>();

    public void updateRecruiting(PotInfoObject potInfoObject) {
        this.potName = potInfoObject.getPotName();
        this.ottType = potInfoObject.getOttType();
        this.price = potInfoObject.getPrice();
        this.recruit = potInfoObject.getRecruit();
        this.term = potInfoObject.getTerm();
    }

    public void updateOngoing(PotInfoObject potInfoObject) {
        this.ottId = potInfoObject.getOttId();
        this.ottPw = potInfoObject.getOttPw();
        this.startDate = potInfoObject.getStartDate();
        this.endDate = potInfoObject.getEndDate();
    }

    public void addAdditionalInfo(PotStartForm potStartForm) {
        this.ottId = potStartForm.getOttId();
        this.ottPw = potStartForm.getOttPw();
        this.startDate = potStartForm.getStartDate();
        this.endDate = potStartForm.getEndDate();
    }

    public void changeState() {
        this.state = State.ONGOING;
    }

    @Builder
    public Pot(String potName, String ottType, Integer price, Integer recruit, Integer term, Integer remain, String ottId,
               String ottPw, LocalDateTime startDate, LocalDateTime endDate, User master, State state) {
        this.potName = potName;
        this.ottType = ottType;
        this.price = price;
        this.recruit = recruit;
        this.term = term;
        this.remain = remain;
        this.ottId = ottId;
        this.ottPw = ottPw;
        this.startDate = startDate;
        this.endDate = endDate;
        this.master = master;
        this.state = state;
    }

    public void apporveUser(){
        this.remain -= 1;
    }
}
