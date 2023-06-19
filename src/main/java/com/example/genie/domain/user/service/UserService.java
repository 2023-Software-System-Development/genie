package com.example.genie.domain.user.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.repository.PotRepository;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private ApplyRepository applyRepository;
    private PotRepository potRepository;
    private final UserUtils userUtils;

    //user의 Apply List 가져오기
    public List<PotInfoObject> getUserApplyPotList(Long userId){
        List<Apply> applyList =  applyRepository.findByApplicant_IdAndState(userId, State.APPROVED);
        List<PotInfoObject> potList = new ArrayList<>();
        for(Apply apply : applyList){
            potList.add(PotMapper.toPotInfoObject(apply.getPot(), false));
        }
        return potList;
    }

    //user
    public List<PotInfoObject> getUserPotList(Long userId){
        List<Pot> potList = potRepository.findByMasterId(userId);
        List<PotInfoObject> potInfoObjectList = new ArrayList<>();
        for(Pot pot : potList){
            potInfoObjectList.add(PotMapper.toPotInfoObject(pot, true));
        }
        return potInfoObjectList;
    }

    public String getUserNickName (Authentication authentication) {
        User user = userUtils.getUser(authentication);
        return user.getUserNickName();
    }
}
