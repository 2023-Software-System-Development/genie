package com.example.genie.domain.reliability.mapper;

import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.dto.ReliabilityInfoDto;
import com.example.genie.domain.user.entity.User;

public class ReliabilityMapper {

    public static ReliabilityInfoDto toReliabilityInfoObject(Reliability reliability) {
        return ReliabilityInfoDto.builder()
                .history(reliability.getHistory())
                .score(reliability.getScore())
                .build();
    }

    public static Reliability mapToReliabilityWithUser(User user,  String history, Integer score) {
        return Reliability.builder()
                .user(user)
                .history(history)
                .score(score)
                .build();
    }
}
