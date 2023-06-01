package com.example.genie.domain.pot.repository;

import com.example.genie.domain.pot.entity.Pot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PotRepositoryCustom {

    Page<Pot> findListBySearch(String searchText, Pageable pageable);
}
