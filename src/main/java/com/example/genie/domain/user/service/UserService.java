package com.example.genie.domain.user.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.auth.service.Role;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.repository.PotRepository;
import com.example.genie.domain.user.dto.UserInfo;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.mapper.UserMapper;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private ApplyRepository applyRepository;
    private PotRepository potRepository;
    private final UserUtils userUtils;

    public User findLoginUser(Authentication authentication){
        return userUtils.getUser(authentication);
    }
    //user가 가입한 Pot List 가져오기
    public Page<PotObject> getUserApplyPotList(Authentication authentication, Pageable pageable){
        User user = userUtils.getUser(authentication);
        Page<Pot> potList = applyRepository.findPotByUser_Id(user.getId(), State.APPROVED, pageable);
        Page<PotObject> potObjects = potList.map(pot -> PotMapper.toPotObject(pot));
        return potObjects;
    }

    //user가 만든 Pot List 가져오기
    public Page<PotObject> getUserPotList(Authentication authentication, Pageable pageable){
        User user = userUtils.getUser(authentication);
        Page<Pot> potList = potRepository.findByMasterId(user.getId(), pageable);
        Page<PotObject> potObjects = potList.map(pot -> PotMapper.toPotObject(pot));
        return potObjects;
    }

    public String getUserNickName (Authentication authentication) {
        User user = userUtils.getUser(authentication);
        return user.getUserNickName();
    }

    public List<UserInfo> getAllUser(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::toUserInfo).collect(Collectors.toList());
    }

    public UserInfo getUserInfo(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not founded"));
        return UserMapper.toUserInfo(user);
    }
}
