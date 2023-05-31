package com.example.genie.domain.pot.mapper;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;

public class PotMapper {

    public static PotObject toPotObject(Pot pot) {
        return PotObject.builder()
                .potId(pot.getId())
                .potName(pot.getPotName())
                .ottType(pot.getOttType())
                .recruit(pot.getRecruit())
                .term(pot.getTerm())
                .masterName(pot.getMaster().getUserName())
                .masterId(pot.getMaster().getId())
                .build();

    }

    public static PotInfoObject toPotInfoObject(Pot pot) {
        return PotInfoObject.builder()
                .potId(pot.getId())
                .potName(pot.getPotName())
                .ottType(pot.getOttType())
                .recruit(pot.getRecruit())
                .term(pot.getTerm())
                .build();

    }
}
