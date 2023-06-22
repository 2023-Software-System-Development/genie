package com.example.genie.domain.chat.entity;

import com.example.genie.common.domain.BaseEntity;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "chat")
@Entity
@DynamicUpdate
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pot_id")
    Pot pot;

    @Builder
    public Chat(String content, User sender, Pot pot) {
        this.content = content;
        this.sender = sender;
        this.pot = pot;
    }
}
