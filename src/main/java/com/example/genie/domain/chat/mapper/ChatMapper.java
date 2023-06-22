package com.example.genie.domain.chat.mapper;

import com.example.genie.domain.chat.entity.Chat;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.user.entity.User;


public class ChatMapper {
    public static Chat mapToChatMessageWithUserAndPot(User user, String message, Pot pot) {
        return Chat.builder()
                .sender(user)
                .content(message)
                .pot(pot)
                .build();
    }
}
