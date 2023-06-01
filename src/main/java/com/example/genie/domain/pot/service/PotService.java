package com.example.genie.domain.pot.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.*;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.repository.PotRepository;
//import com.example.genie.domain.pot.repository.PotRepositoryCustom;
import com.example.genie.domain.pot.repository.PotRepositoryCustom;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<PotObject> getPotList(String ottType, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 6); // <- Sort 추가
        Page<Pot> pots = potRepository.findListByOttType(ottType, pageable);
        return pots.map(PotMapper::toPotObject);
    }

    public Page<PotObject> getPotListBySearch(PotSearchForm potSearchForm, Pageable pageable) {
        Page<Pot> pots = potRepositoryCustom.findListBySearch(potSearchForm.getSearchText(), pageable);
        return pots.map(PotMapper::toPotObject);
    }

    public PotInfoObject getPot(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        boolean isMaster = user.equals(pot.getMaster());
        return PotMapper.toPotInfoObject(pot, isMaster);
    }

    public void getPotStarted(Long potId, PotStartForm potStartForm) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        pot.changeState();
        pot.addAdditionalInfo(potStartForm);
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
