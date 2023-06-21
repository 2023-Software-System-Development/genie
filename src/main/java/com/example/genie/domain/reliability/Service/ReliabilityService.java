package com.example.genie.domain.reliability.Service;
import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.reliability.mapper.ReliabilityMapper;
import com.example.genie.domain.reliability.model.ReliabilityInfoObject;
import com.example.genie.domain.reliability.repository.ReliabilityRepository;
import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReliabilityService {
    private final ReliabilityRepository reliabilityRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    public Page<ReliabilityInfoObject> getUserReliabilities(Authentication authentication, Pageable pageable){
        User user = userUtils.getUser(authentication);
        Page<Reliability> reliabilities = reliabilityRepository.findByUserOrderByCreatedDateDesc(user.getId(), pageable);
        return reliabilities.map(ReliabilityMapper::toReliabilityInfoObject);
    }

    public void addReliability(User user, ReliabilityInfoObject reliabilityInfoObject){
        Reliability reliability = Reliability.builder().user(user).score(reliabilityInfoObject.getScore()).history(reliabilityInfoObject.getHistory()).build();
        user.updateReliability(user.getReliabilityScore() + reliabilityInfoObject.getScore());
        reliabilityRepository.save(reliability);
    }
}