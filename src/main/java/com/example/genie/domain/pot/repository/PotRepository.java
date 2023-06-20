package com.example.genie.domain.pot.repository;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface PotRepository extends JpaRepository<Pot, Long> {
    public Page<Pot> findByMasterId(Long userId, Pageable pageable);
}
