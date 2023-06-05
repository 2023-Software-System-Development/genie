package com.example.genie.domain.reliability.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReliabilityInfoObject {

    private final String history;
    private final Integer score;

    @Builder
    public ReliabilityInfoObject (String history, Integer score) {
        this.history = history;
        this.score = score;
    }
}
