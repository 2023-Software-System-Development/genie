package com.example.genie.domain.reliability.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReliabilityInfoDto {

    private final String history;
    private final Integer score;

    @Builder
    public ReliabilityInfoDto (String history, Integer score) {
        this.history = history;
        this.score = score;
    }
}
