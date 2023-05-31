package com.example.genie.domain.apply.repository;

import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    public List<Apply> findByPot_Id(String potId);
    public List<Apply> findByPot(Pot pot, Pageable pageable);
    public Apply findByPot_IdAndApplicant_Id(String potId, String userId);
    public List<Apply> findByStateAndPot(State state, Pot pot);

    public int deleteByStateAndPot(State state, Pot pot);
}
