package com.example.genie.domain.pot.mapper;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.pot.dto.PotInfoDto;
import com.example.genie.domain.pot.dto.PotDto;
import com.example.genie.domain.user.entity.User;

import static com.example.genie.domain.pot.entity.State.RECRUITING;

public class PotMapper {

    public static PotDto toPotObject(Pot pot) {
        return PotDto.builder()
                .potId(pot.getId())
                .potName(pot.getPotName())
                .ottType(pot.getOttType())
                .recruit(pot.getRecruit())
                .remain(pot.getRemain())
                .term(pot.getTerm())
                .masterName(pot.getMaster().getUserNickName())
                .masterId(pot.getMaster().getId())
                .wish(false)
                .build();

    }

    public static PotInfoDto toPotInfoObject(Pot pot, boolean isMaster, boolean canSeeCredentials) {
        return PotInfoDto.builder()
                .potId(pot.getId())
                .potName(pot.getPotName())
                .ottType(pot.getOttType())
                .price(pot.getPrice())
                .recruit(pot.getRecruit())
                .remain(pot.getRemain())
                .term(pot.getTerm())
                // OTT 계정/계좌 정보는 master 또는 승인된 멤버에게만 노출
                .ottId(canSeeCredentials ? pot.getOttId() : null)
                .ottPw(canSeeCredentials ? pot.getOttPw() : null)
                .startDate(pot.getStartDate())
                .endDate(pot.getEndDate())
                .bankName(canSeeCredentials ? pot.getBankName() : null)
                .accountNumber(canSeeCredentials ? pot.getAccountNumber() : null)
                .masterName(pot.getMaster().getUserNickName())
                .masterId(pot.getMaster().getId())
                .isMaster(isMaster)
                .build();

    }

    public static Pot mapToPotWithUser(PotCreateForm potCreateForm, User user) {
        return Pot.builder()
                .potName(potCreateForm.getPotName())
                .ottType(potCreateForm.getOttType())
                .price(potCreateForm.getPrice())
                .recruit(potCreateForm.getRecruit())
                .term(potCreateForm.getTerm())
                .master(user)
                .state(RECRUITING)
                .remain(potCreateForm.getRecruit())
                .build();
    }
}
