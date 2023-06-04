package com.example.genie.domain.pot.repository;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PotRepositoryCustom {

    Page<Pot> findListBySearch(PotSearchForm potSearchForm, Pageable pageable);
}
