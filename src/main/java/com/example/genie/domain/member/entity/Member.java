package com.example.genie.domain.member.entity;

import com.example.genie.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
@DynamicUpdate
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memeber_id")
    private Long id;

    private String memberName;
    private String memberLoginId;
    private String memberPw;
    private String memberNickName;
    private String phoneNumber;
    private String email;
    private LocalDateTime birth;
    private String accountNumber;
    private String bankName;

    @Builder
    public Member(String memberName, String memberLoginId, String memberPw, String memberNickName, String phoneNumber
            , String email, LocalDateTime birth, String accountNumber, String bankName) {
        this.memberName = memberName;
        this.memberLoginId = memberLoginId;
        this.memberPw = memberPw;
        this.memberNickName = memberNickName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
}
