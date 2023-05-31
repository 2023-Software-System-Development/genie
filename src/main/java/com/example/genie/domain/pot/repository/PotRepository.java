package com.example.genie.domain.pot.repository;
import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PotRepository extends JpaRepository<Pot, Long> {

    @Query(value = "SELECT * FROM pot p WHERE p.ott_type = :ottType", nativeQuery = true)
    Page<Pot> findListByOttType(String ottType, Pageable pageable);

}
