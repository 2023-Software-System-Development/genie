package com.example.genie.domain.pot.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.pot.form.PotEditOngoingForm;
import com.example.genie.domain.pot.form.PotEditRecruitingForm;
import com.example.genie.domain.pot.form.PotSearchForm;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.repository.PotRepository;
import com.example.genie.domain.pot.repository.PotRepositoryCustom;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PotService {

    private final PotRepository potRepository;
    private final PotRepositoryCustom potRepositoryCustom;

    private final UserUtils userUtils;

    public void createPot(Authentication authentication, PotCreateForm potCreateForm, BindingResult bindingResult) {
        User user = userUtils.getUser(authentication);
        Pot pot = PotMapper.mapToPotWithUser(potCreateForm, user);
        potRepository.save(pot);
    }

    public void deletePot(Long potId) {
        potRepository.deleteById(potId);
    }

    public List<PotObject> getPotList(String ottType, Pageable pageable) {
        Page<Pot> pots = potRepository.findListByOttType(ottType, pageable);
        return pots.stream()
                .map(PotMapper::toPotObject)
                .collect(Collectors.toList());
    }

    public List<PotObject> getPotListBySearch(PotSearchForm potSearchForm, Pageable pageable) {
        Page<Pot> pots = potRepositoryCustom.findListBySearch(potSearchForm.getSearchText(), pageable);
        return pots.stream()
                .map(PotMapper::toPotObject)
                .collect(Collectors.toList());
    }

    public PotInfoObject getPot(Long potId) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        return PotMapper.toPotInfoObject(pot);
    }

    public PotInfoObject getPotStarted(Long potId) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        pot.changeState();
        return PotMapper.toPotInfoObject(pot);
    }

    public void editRecruitingPot(Long potId, PotEditRecruitingForm potEditRecruitingForm) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        pot.updateRecruiting(potEditRecruitingForm);
    }

    public void editOngoingPot(Long potId, PotEditOngoingForm potEditOngoingForm) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        pot.updateOngoing(potEditOngoingForm);
    }

    public Pot getPotEntity(Long potId) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        return pot;
    }


}
