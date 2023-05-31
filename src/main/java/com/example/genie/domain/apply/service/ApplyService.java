package com.example.genie.domain.apply.service;

import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplyService {
    
    ApplyRepository applyRepository;

    //Apply 생성
    public Apply createApply(User user, Pot pot){
        Apply apply = Apply.builder().state(State.APPLY).applicant(user).pot(pot).build();
        applyRepository.save(apply);
        
        return apply;
    }

    public List<User> getApplyUserList(String potId){
        List<Apply> appyList = applyRepository.findByPot_Id(potId);
        List<User> userList = new ArrayList<>();
        for(Apply apply : appyList){
            userList.add(apply.getApplicant());
        }
        return userList;
    }

    public List<Apply> findApplyList(Pot pot, int pageNum){
        return applyRepository.findByPot(pot, (Pageable) PageRequest.of(pageNum-1, 10));
    }

    public Apply appoveApply(String potId, String userId, int s){
        Apply apply = applyRepository.findByPot_IdAndApplicant_Id(potId, userId);
        State state;
        if(s==0)
            state = State.APPROVED;
        else
            state = State.REJECTED;
        apply.changeState(state);
        return applyRepository.save(apply);
    }

    public List<Apply> getApprovedApplyList(Pot pot){
        List<Apply> applyList = applyRepository.findByStateAndPot(State.REJECTED, pot);
        return applyList;
    }


}
