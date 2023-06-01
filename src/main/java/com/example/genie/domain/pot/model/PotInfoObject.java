package com.example.genie.domain.pot.model;

import lombok.Builder;

import java.time.LocalDateTime;

public class PotInfoObject {
    private final Long potId;
    private final String potName;
    private final String ottType;
    private final Integer price;
    private final Integer recruit;
    private final Integer remain;
    private final Integer term;
    private final String ottId;
    private final String ottPw;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String masterName;
    private final Long masterId;
    private final boolean isMaster;

    @Builder
    public PotInfoObject(Long potId, String potName, String ottType, Integer price, Integer recruit, Integer remain, Integer term,
                         String ottId, String ottPw, LocalDateTime startDate, LocalDateTime endDate,
                         String masterName, Long masterId, boolean isMaster) {
        this.potId = potId;
        this.potName = potName;
        this.ottType = ottType;
        this.price = price;
        this.recruit = recruit;
        this.remain = remain;
        this.term = term;
        this.ottId = ottId;
        this.ottPw = ottPw;
        this.startDate = startDate;
        this.endDate = endDate;
        this.masterName = masterName;
        this.masterId = masterId;
        this.isMaster  = isMaster;
    }

}
