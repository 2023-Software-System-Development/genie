package com.example.genie.domain.interest.repository;

import com.example.genie.domain.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    public Interest findByPot_IdAndUser_Id(Long potId, Long userId);
    public List<Interest> findByUser_Id(Long userId);
}
