package com.example.genie.domain.reliability.service;
import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.reliability.mapper.ReliabilityMapper;
import com.example.genie.domain.reliability.dto.ReliabilityInfoDto;
import com.example.genie.domain.reliability.repository.ReliabilityRepository;
import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReliabilityService {
    private final ReliabilityRepository reliabilityRepository;
    private final UserUtils userUtils;

    @Transactional(readOnly = true)
    public Page<ReliabilityInfoDto> getUserReliabilities(Authentication authentication, Pageable pageable){
        User user = userUtils.getUser(authentication);
        Page<Reliability> reliabilities = reliabilityRepository.findByUserOrderByCreatedDateDesc(user.getId(), pageable);
        return reliabilities.map(ReliabilityMapper::toReliabilityInfoObject);
    }

    @Transactional
    public void addReliability(User user, ReliabilityInfoDto reliabilityInfoObject){
        Reliability reliability = Reliability.builder().user(user).score(reliabilityInfoObject.getScore()).history(reliabilityInfoObject.getHistory()).build();
        user.updateReliability(user.getReliabilityScore() + reliabilityInfoObject.getScore());
        reliabilityRepository.save(reliability);
    }
}
