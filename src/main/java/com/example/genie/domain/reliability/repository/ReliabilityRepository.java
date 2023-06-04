package com.example.genie.domain.reliability.repository;

import com.example.genie.domain.reliability.entity.Reliability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReliabilityRepository extends JpaRepository<Reliability, Long> {
    @Query(value = "SELECT * FROM reliability re WHERE re.user_id = :userId", nativeQuery = true)
    Page<Reliability> findByUserOrderByCreatedDateDesc(Long userId, Pageable pageable);
}
