package com.example.genie.domain.user.entity;

import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.auth.service.Role;
import com.example.genie.domain.reliability.entity.Reliability;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
@DynamicUpdate
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;
    private String userLoginId;
    private String userPw;
    private String userNickName;
    private String phoneNumber;
    private String email;
    private LocalDateTime birth;
    private Role role;
    Integer reliabilityScore;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Apply> applies = new ArrayList<>();

    @Builder
    public User(String userName, String userLoginId, String userPw, String userNickName, String phoneNumber, String email, LocalDateTime birth, String accountNumber, String bankName, Integer reliabilityScore, Role role) {
        this.userName = userName;
        this.userLoginId = userLoginId;
        this.userPw = userPw;
        this.userNickName = userNickName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.reliabilityScore = reliabilityScore;
        this.role = role;
    }

}
