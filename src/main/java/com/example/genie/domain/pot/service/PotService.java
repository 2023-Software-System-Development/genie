package com.example.genie.domain.pot.service;

import com.example.genie.common.exception.CustomException;
import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.interest.repository.InterestRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.*;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.repository.PotRepository;
//import com.example.genie.domain.pot.repository.PotRepositoryCustom;
import com.example.genie.domain.pot.repository.PotRepositoryCustom;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PotService {

    private final PotRepository potRepository;
    private final PotRepositoryCustom potRepositoryCustom;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final ApplyRepository applyRepository;
    private final InterestRepository interestRepository;

    public void createPot(Authentication authentication, PotCreateForm potCreateForm, BindingResult bindingResult) {
        User user = userUtils.getUser(authentication);
        Pot pot = PotMapper.mapToPotWithUser(potCreateForm, user);
        potRepository.save(pot);
    }

    public void deletePot(Long potId) {
        potRepository.deleteById(potId);
    }

    public Page<PotObject> getPotListBySearch(PotSearchForm potSearchForm, Pageable pageable) {
        String searchText = potSearchForm.getSearchText();
        String searchType = potSearchForm.getSearchType();

        if (("remain".equals(searchType) || "term".equals(searchType)) && !isNumeric(searchText)) {
            throw new CustomException("숫자를 입력해주세요");
        }

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 8); // <- Sort 추가
        Page<Pot> pots = potRepositoryCustom.findListBySearch(potSearchForm, pageable);
        return pots.map(PotMapper::toPotObject);
    }

    public Page<PotObject> getPotListBySearch(Authentication authentication, PotSearchForm potSearchForm, Pageable pageable) {

        String searchText = potSearchForm.getSearchText();
        String searchType = potSearchForm.getSearchType();

        if (("remain".equals(searchType) || "term".equals(searchType)) && !isNumeric(searchText)) {
            throw new CustomException("숫자를 입력해주세요");
        }

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 8); // <- Sort 추가
        Page<Pot> pots = potRepositoryCustom.findListBySearch(potSearchForm, pageable);
        User user = userUtils.getUser(authentication);
        Interest interest;
        Page<PotObject> potObjects = pots.map(PotMapper::toPotObject);
        for(PotObject pot : potObjects){
            interest = interestRepository.findByPot_IdAndUser_Id(pot.potId, user.getId());
            if(interest != null){
                pot.setWish(true);
            }
        }

        return potObjects;
    }

    public PotInfoObject getPot(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        boolean isMaster = user.equals(pot.getMaster());
        return PotMapper.toPotInfoObject(pot, isMaster);
    }

    @Transactional
    public void getPotStarted(Long potId, PotStartForm potStartForm) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        pot.changeState();
        pot.addAdditionalInfo(potStartForm);
        applyRepository.deleteByStateAndPot_Id(State.REJECTED, potId);
        applyRepository.deleteByStateAndPot_Id(State.APPLY, potId);
        potRepository.save(pot);
    }

    public Pot getPotEntity(Long potId) {
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        return pot;
    }

    // 편의 메서드
    private boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
