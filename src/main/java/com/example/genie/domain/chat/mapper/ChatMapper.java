package com.example.genie.domain.chat.mapper;

import com.example.genie.domain.chat.entity.ChatRoom;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.user.entity.User;

import java.util.List;

import static com.example.genie.domain.pot.entity.State.RECRUITING;

public class ChatMapper {

    public static ChatRoom mapToChatRoomWithUser(List<User> potUsers) {
        return ChatRoom.builder()
                .senders(potUsers)
                .receivers(potUsers)
                .build();
    }
}
