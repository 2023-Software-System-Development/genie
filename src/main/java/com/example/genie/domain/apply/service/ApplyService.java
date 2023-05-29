package com.example.genie.domain.apply.service;

import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class ApplyService {

    ApplyRepository applyRepository;
    UserRepository userRepository;

   /* public Apply createApply(Long potId, Long userId){
        User applicant = userRepository.getOne(userId);
        Pot pot potRepository.getOne(potId);

        Apply apply = Apply.builder()
                .state(State.APPLY)
                .applicant(applicant)
                .pot(pot).build();
        return applyRepository.save(apply);
    }*/


}
