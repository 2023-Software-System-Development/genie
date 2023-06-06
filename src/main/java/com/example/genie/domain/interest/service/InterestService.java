package com.example.genie.domain.interest.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.interest.mapper.InterestMapper;
import com.example.genie.domain.interest.repository.InterestRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final UserUtils userUtils;
    private final PotService potService;
    private final InterestRepository interestRepository;

    public void saveInterest(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPotEntity(potId);
        Interest interest = InterestMapper.mapToPotWithUser(user,pot);
        interestRepository.save(interest);
    }
}
