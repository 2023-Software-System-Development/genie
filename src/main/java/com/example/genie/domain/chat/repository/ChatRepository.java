package com.example.genie.domain.chat.repository;

import com.example.genie.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "SELECT * FROM chat c WHERE c.pot_id = :potId ORDER BY r.created_date asc", nativeQuery = true)
    List<Chat> findChatByPotId(Long potId);
}
