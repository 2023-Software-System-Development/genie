package com.example.genie.domain.user.dto;

import com.example.genie.domain.auth.service.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class UserInfo {
    private Long id;
    private String userName;
    private String userLoginId;
    private String userNickName;
    private String phoneNumber;
    private String email;
    private LocalDateTime birth;
    private Integer reliabilityScore;

    @Builder
    public UserInfo(Long id, String userName, String userLoginId, String userNickName, String phoneNumber, String email, LocalDateTime birth, Integer reliabilityScore) {
        this.id = id;
        this.userName = userName;
        this.userLoginId = userLoginId;
        this.userNickName = userNickName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
        this.reliabilityScore = reliabilityScore;
    }
}
