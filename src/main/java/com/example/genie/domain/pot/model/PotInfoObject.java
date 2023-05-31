package com.example.genie.domain.pot.model;

import lombok.Builder;

import java.time.LocalDateTime;

public class PotInfoObject {
    private final Long potId;
    private final String potName;
    private final String ottType;
    private final Integer price;
    private final Integer recruit;
    private final Integer term;
    private final String ott_id;
    private final String ott_pwd;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String masterName;
    private final Long masterId;

    @Builder
    public PotInfoObject(Long potId, String potName, String ottType, Integer price, Integer recruit, Integer term,
                         String ott_id, String ott_pwd, LocalDateTime startDate, LocalDateTime endDate,
                         String masterName, Long masterId) {
        this.potId = potId;
        this.potName = potName;
        this.ottType = ottType;
        this.price = price;
        this.recruit = recruit;
        this.term = term;
        this.ott_id = ott_id;
        this.ott_pwd = ott_pwd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.masterName = masterName;
        this.masterId = masterId;
    }

}
