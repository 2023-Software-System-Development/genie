package com.example.genie.domain.user.entity;

import com.example.genie.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String userId;
    private String userPw;
    private String userNickName;
    private String phoneNumber;
    private String email;
    private LocalDateTime birth;
    private String accountNumber;
    private String bankName;

    @Builder
    public User(String userName, String userId, String userPw, String userNickName, String phoneNumber
            , String email, LocalDateTime birth, String accountNumber, String bankName) {
        this.userName = userName;
        this.userId = userId;
        this.userPw = userPw;
        this.userNickName = userNickName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
}
