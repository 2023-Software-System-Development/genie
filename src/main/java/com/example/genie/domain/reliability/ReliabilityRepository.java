package com.example.genie.domain.reliability;

import com.example.genie.domain.reliability.entity.Reliability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReliabilityRepository extends JpaRepository<Reliability, Long> {
    List<Reliability> findByUserOOrderByCreatedDateDesc(); //유저의 신뢰도 내역 불러오기
}
