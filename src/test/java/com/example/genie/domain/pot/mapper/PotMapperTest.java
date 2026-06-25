package com.example.genie.domain.pot.mapper;

import com.example.genie.domain.pot.dto.PotInfoDto;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotStartForm;
import com.example.genie.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PotMapper.toPotInfoObject 의 자격증명 노출 제어(보안) 단위 테스트.
 * OTT 계정/계좌 정보는 master 또는 승인된 멤버에게만 노출되어야 한다.
 */
class PotMapperTest {

    private Pot potWithCredentials() {
        User master = User.builder().userNickName("팟장클레이").build();
        Pot pot = Pot.builder()
                .potName("넷플릭스 같이봐요")
                .ottType("Netflix")
                .price(17000)
                .recruit(4)
                .remain(2)
                .term(3)
                .master(master)
                .build();

        PotStartForm form = new PotStartForm();
        form.setOttId("netflix@id.com");
        form.setOttPw("secret-pw");
        form.setBankName("신한");
        form.setAccountNumber("110-123-456789");
        form.setStartDate("2026-06-01");
        form.setEndDate("2026-09-01");
        pot.addAdditionalInfo(form); // ottId/ottPw/계좌/은행/기간 채움

        return pot;
    }

    @Test
    @DisplayName("멤버가 아니면(canSeeCredentials=false) OTT 계정과 계좌 정보가 가려진다")
    void hidesCredentialsForNonMember() {
        Pot pot = potWithCredentials();

        PotInfoDto dto = PotMapper.toPotInfoObject(pot, false, false);

        // 민감 정보는 null 로 가려져야 한다
        assertThat(dto.getOttId()).isNull();
        assertThat(dto.getOttPw()).isNull();
        assertThat(dto.getBankName()).isNull();
        assertThat(dto.getAccountNumber()).isNull();
        // 공개 정보는 그대로 노출된다
        assertThat(dto.getPotName()).isEqualTo("넷플릭스 같이봐요");
        assertThat(dto.getOttType()).isEqualTo("Netflix");
        assertThat(dto.getRemain()).isEqualTo(2);
        assertThat(dto.isMaster()).isFalse();
    }

    @Test
    @DisplayName("master/승인 멤버(canSeeCredentials=true)는 OTT 계정과 계좌 정보를 볼 수 있다")
    void showsCredentialsForMember() {
        Pot pot = potWithCredentials();

        PotInfoDto dto = PotMapper.toPotInfoObject(pot, true, true);

        assertThat(dto.getOttId()).isEqualTo("netflix@id.com");
        assertThat(dto.getOttPw()).isEqualTo("secret-pw");
        assertThat(dto.getBankName()).isEqualTo("신한");
        assertThat(dto.getAccountNumber()).isEqualTo("110-123-456789");
        assertThat(dto.isMaster()).isTrue();
    }
}
