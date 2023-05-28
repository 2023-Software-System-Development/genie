package com.example.genie.domain.apply.repository;

import com.example.genie.domain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findByPot_Id(String potId); //팟의 신청 목록 조회

}
