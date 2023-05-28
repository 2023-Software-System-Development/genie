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
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pot_id")
    private Pot pot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String contents;

    @Builder
    public Chat(Pot pot, User user, String contents) {
        this.pot = pot;
        this.user = user;
        this.contents = contents;
    }
}
