package com.example.genie.domain.user.mapper;

import com.example.genie.domain.user.dto.UserInfo;
import com.example.genie.domain.user.entity.User;

public class UserMapper {
    public static UserInfo toUserInfo(User user){
        return UserInfo.builder()
                .id(user.getId())
                .userLoginId(user.getUserLoginId())
                .userName(user.getUserName())
                .userNickName(user.getUserNickName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .reliabilityScore(user.getReliabilityScore())
                .build();
    }
}
