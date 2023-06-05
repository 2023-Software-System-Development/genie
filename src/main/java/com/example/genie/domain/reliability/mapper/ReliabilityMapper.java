package com.example.genie.domain.reliability.mapper;

import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.model.ReliabilityInfoObject;

public class ReliabilityMapper {

    public static ReliabilityInfoObject toReliabilityInfoObject(Reliability reliability) {
        return ReliabilityInfoObject.builder()
                .history(reliability.getHistory())
                .score(reliability.getScore())
                .build();
    }
}
