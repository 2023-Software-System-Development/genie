package com.example.genie.domain.pot.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PotEditOngoingForm { //진행중

    private String ottId;

    private String ottPw;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
