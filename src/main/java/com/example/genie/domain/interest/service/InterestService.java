package com.example.genie.domain.interest.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.interest.mapper.InterestMapper;
import com.example.genie.domain.interest.repository.InterestRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.dto.PotDto;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final UserUtils userUtils;
    private final PotService potService;
    private final InterestRepository interestRepository;

    @Transactional
    public Interest saveInterest(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        // 이미 찜한 경우 중복 저장하지 않음
        Interest existing = interestRepository.findByPot_IdAndUser_Id(potId, user.getId());
        if (existing != null) {
            return existing;
        }
        Pot pot = potService.getPotEntity(potId);
        Interest interest = InterestMapper.mapToPotWithUser(user, pot);
        return interestRepository.save(interest);
    }

    @Transactional
    public void deleteInterest(Authentication authentication, Long potId){
        User user = userUtils.getUser(authentication);
        Interest interest = interestRepository.findByPot_IdAndUser_Id(potId, user.getId());
        if (interest != null) {
            interestRepository.delete(interest);
        }
    }

    @Transactional(readOnly = true)
    public Page<PotDto> getUserInterestPotList(Authentication authentication, Pageable pageable){
        User user = userUtils.getUser(authentication);
        Page<Pot> pots = interestRepository.findPotByUser_Id(user.getId(), PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize()));
        return pots.map(PotMapper::toPotObject);
    }

}
