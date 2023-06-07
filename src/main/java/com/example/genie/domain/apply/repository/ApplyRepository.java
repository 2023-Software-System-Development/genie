package com.example.genie.domain.apply.repository;

import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    public List<Apply> findByPot_Id(Long potId);
    public Apply findByPot_IdAndApplicant_Id(Long potId, Long userId);
    public List<Apply> findByStateAndPot_Id(State state, Long potId);
    public void deleteByStateAndPot_Id(State state, Long potId);
}
