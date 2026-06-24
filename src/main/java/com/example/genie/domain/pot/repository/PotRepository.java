package com.example.genie.domain.pot.repository;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PotRepository extends JpaRepository<Pot, Long> {
    public Page<Pot> findByMasterId(Long userId, Pageable pageable);

    // 동시 승인으로 인한 정원 초과(lost update)를 막기 위해 행 단위 비관적 락으로 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Pot p where p.id = :id")
    Optional<Pot> findByIdForUpdate(@Param("id") Long id);
}
