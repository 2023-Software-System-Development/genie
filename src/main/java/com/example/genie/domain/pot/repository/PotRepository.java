package com.example.genie.domain.pot.repository;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotRepository extends JpaRepository<Pot, Long> {
}
