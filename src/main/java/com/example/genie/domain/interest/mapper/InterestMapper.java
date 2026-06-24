package com.example.genie.domain.interest.mapper;

import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.user.entity.User;

public class InterestMapper {

    public static Interest mapToPotWithUser(User user, Pot pot) {
        return Interest.builder()
                .user(user)
                .pot(pot)
                .build();
    }

}
