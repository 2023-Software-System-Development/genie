package com.example.genie.domain.pot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PotInfoDto {
    private final Long potId;
    private final String potName;
    private final String ottType;
    private final Integer price;
    private final Integer recruit;
    private final Integer remain;
    private final Integer term;
    private final String ottId;
    private final String ottPw;
    private final String startDate;
    private final String endDate;
    private final String bankName;
    private final String accountNumber;
    private final String masterName;
    private final Long masterId;
    private final boolean isMaster;

    @Builder
    public PotInfoDto(Long potId, String potName, String ottType, Integer price, Integer recruit, Integer remain, Integer term,
                         String ottId, String ottPw, String startDate, String endDate, String bankName, String accountNumber,
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
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.masterName = masterName;
        this.masterId = masterId;
        this.isMaster  = isMaster;
    }

}
