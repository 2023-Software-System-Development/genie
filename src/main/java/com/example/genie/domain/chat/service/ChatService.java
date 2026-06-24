package com.example.genie.domain.chat.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.chat.entity.Chat;
import com.example.genie.domain.chat.mapper.ChatMapper;
import com.example.genie.domain.chat.repository.ChatRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserUtils userUtils;
    private final ChatRepository chatRepository;
    private final PotService potService;
    private final ApplyRepository applyRepository;

    @Transactional
    public void saveChatMessage(Authentication authentication, String message, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPotEntity(potId);
        Chat chatMessage = ChatMapper.mapToChatMessageWithUserAndPot(user, message, pot);
        chatRepository.save(chatMessage);
    }

    @Transactional(readOnly = true)
    public List<String> getChatMessages(Long potId) {
        List<String> message = new ArrayList<>();
        List<Chat> chats = chatRepository.findChatByPotId(potId);
        for (Chat chat : chats) {
            message.add(chat.getContent());
        }
        return message;
    }

    // 채팅 참여 권한: 해당 팟의 master 또는 승인된 멤버만 허용
    @Transactional(readOnly = true)
    public boolean isMember(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPotEntity(potId);
        if (pot.getMaster() != null && pot.getMaster().getId().equals(user.getId())) {
            return true;
        }
        Apply apply = applyRepository.findByPot_IdAndApplicant_Id(potId, user.getId());
        return apply != null && apply.getState() == State.APPROVED;
    }
}
