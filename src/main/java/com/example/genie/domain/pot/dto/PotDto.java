package com.example.genie.domain.pot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PotDto {

    public final Long potId;
    private final String potName;
    private final String ottType;
    private final Integer recruit;
    private final Integer remain;
    private final Integer term;
    private final String masterName;
    private final Long masterId;
    private boolean wish;

    @Builder
    public PotDto(Long potId, String potName, String ottType, Integer recruit, Integer remain, Integer term, String masterName, Long masterId, boolean wish) {
        this.potId = potId;
        this.potName = potName;
        this.ottType = ottType;
        this.recruit = recruit;
        this.remain = remain;
        this.term = term;
        this.masterName = masterName;
        this.masterId = masterId;
        this.wish = wish;
    }
    public void setWish(boolean wish){
        this.wish = wish;
    }
}
