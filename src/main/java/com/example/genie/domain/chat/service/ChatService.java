package com.example.genie.domain.chat.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.chat.entity.Chat;
import com.example.genie.domain.chat.mapper.ChatMapper;
import com.example.genie.domain.chat.repository.ChatRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserUtils userUtils;
    private final ChatRepository chatRepository;
    private final PotService potService;

    public void saveChatMessage(Authentication authentication, String message, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPotEntity(potId);
        Chat chatMessage = ChatMapper.mapToChatMessageWithUserAndPot(user, message, pot);
        chatRepository.save(chatMessage);
    }

    public List<String> getChatMessages(Long potId) {
        List<String> message = new ArrayList<>();
        List<Chat> chats = chatRepository.findChatByPotId(potId);
        for (Chat chat : chats) {
            message.add(chat.getContent());
        }
        return message;
    }
}
