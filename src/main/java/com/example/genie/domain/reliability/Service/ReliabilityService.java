package com.example.genie.domain.reliability.Service;

import com.example.genie.domain.reliability.ReliabilityRepository;
import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReliabilityService {
    ReliabilityRepository reliabilityRepository;

    public List<Reliability> getUserReliabilities(User user){
        return reliabilityRepository.findByUserOOrderByCreatedDateDesc();
    }
}
