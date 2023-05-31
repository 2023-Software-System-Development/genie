package com.example.genie.domain.pot.mapper;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.user.entity.User;

import static com.example.genie.domain.pot.entity.State.RECRUITING;

public class PotMapper {

    public static PotObject toPotObject(Pot pot) {
        return PotObject.builder()
                .potId(pot.getId())
                .potName(pot.getPotName())
                .ottType(pot.getOttType())
                .recruit(pot.getRecruit())
                .term(pot.getTerm())
                .masterName(pot.getMaster().getUserNickName())
                .masterId(pot.getMaster().getId())
                .build();

    }

    public static PotInfoObject toPotInfoObject(Pot pot) {
        return PotInfoObject.builder()
                .potId(pot.getId())
                .potName(pot.getPotName())
                .ottType(pot.getOttType())
                .price(pot.getPrice())
                .recruit(pot.getRecruit())
                .remain(pot.getRemain())
                .term(pot.getTerm())
                .ott_id(pot.getOtt_id())
                .ott_pwd(pot.getOtt_pwd())
                .startDate(pot.getStartDate())
                .endDate(pot.getEndDate())
                .masterName(pot.getMaster().getUserNickName())
                .masterId(pot.getMaster().getId())
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
                .build();
    }
}