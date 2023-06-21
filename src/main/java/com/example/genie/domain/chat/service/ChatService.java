package com.example.genie.domain.chat.service;

import com.example.genie.domain.chat.entity.ChatRoom;
import com.example.genie.domain.chat.mapper.ChatMapper;
import com.example.genie.domain.chat.repository.ChatRoomRepository;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public void createChatRoom(List<User> userList) {
        ChatRoom chatRoom = ChatMapper.mapToChatRoomWithUser(userList);
        chatRoomRepository.save(chatRoom);
    }
}
