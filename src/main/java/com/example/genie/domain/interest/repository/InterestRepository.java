package com.example.genie.domain.interest.repository;

import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    public Interest findByPot_IdAndUser_Id(Long potId, Long userId);

    @Query(value = "select i.pot from Interest i where i.user.id = :userId")
    public Page<Pot> findPotByUser_Id(Long userId, Pageable pageable);
}
