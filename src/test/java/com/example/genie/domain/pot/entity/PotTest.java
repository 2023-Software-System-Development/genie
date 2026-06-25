package com.example.genie.domain.pot.entity;

import com.example.genie.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pot 도메인 로직(정원 차감) 단위 테스트.
 */
class PotTest {

    private Pot potWithRemain(int remain) {
        return Pot.builder()
                .potName("테스트 팟")
                .ottType("Netflix")
                .recruit(4)
                .remain(remain)
                .master(User.builder().userNickName("팟장").build())
                .build();
    }

    @Test
    @DisplayName("approveUser는 남은 자리를 1 감소시킨다")
    void approveUserDecrementsRemain() {
        Pot pot = potWithRemain(2);

        pot.approveUser();

        assertThat(pot.getRemain()).isEqualTo(1);
    }

    @Test
    @DisplayName("남은 자리가 0이면 approveUser는 IllegalStateException을 던진다")
    void approveUserThrowsWhenFull() {
        Pot pot = potWithRemain(0);

        assertThatThrownBy(pot::approveUser)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("남은 자리");
    }
}
