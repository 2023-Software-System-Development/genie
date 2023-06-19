package com.example.genie.domain.interest.repository;

import com.example.genie.domain.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    public Interest findByPot_IdAndUser_Id(Long potId, Long userId);
}
