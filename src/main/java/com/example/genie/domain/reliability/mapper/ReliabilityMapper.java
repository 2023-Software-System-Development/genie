package com.example.genie.domain.reliability.mapper;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.model.ReliabilityInfoObject;
import com.example.genie.domain.report.model.ReportInfoObject;
import com.example.genie.domain.user.entity.User;

import static com.example.genie.domain.pot.entity.State.RECRUITING;

public class ReliabilityMapper {

    public static ReliabilityInfoObject toReliabilityInfoObject(Reliability reliability) {
        return ReliabilityInfoObject.builder()
                .history(reliability.getHistory())
                .score(reliability.getScore())
                .build();
    }

//    public static Reliability mapToReliabilityWithUser(ReportInfoObject reportInfoObject, User user) {
//        return Reliability.builder()
//                .user(user)
//                .history(reliability.getHistory())
//                .score(reportInfoObject.)
//                .build();
//    }
}
