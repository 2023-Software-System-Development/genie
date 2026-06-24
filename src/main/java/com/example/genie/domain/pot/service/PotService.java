package com.example.genie.domain.pot.service;

import com.example.genie.common.exception.CustomException;
import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.interest.repository.InterestRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.*;
import com.example.genie.domain.pot.mapper.PotMapper;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.repository.PotRepository;
import com.example.genie.domain.pot.repository.PotRepositoryCustom;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PotService {

    private final PotRepository potRepository;
    private final PotRepositoryCustom potRepositoryCustom;
    private final UserUtils userUtils;
    private final ApplyRepository applyRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public void createPot(Authentication authentication, PotCreateForm potCreateForm, BindingResult bindingResult) {
        User user = userUtils.getUser(authentication);
        Pot pot = PotMapper.mapToPotWithUser(potCreateForm, user);
        potRepository.save(pot);
    }

    @Transactional
    public void deletePot(Authentication authentication, Long potId) {
        Pot pot = getPotOwnedBy(authentication, potId);
        potRepository.delete(pot);
    }

    @Transactional(readOnly = true)
    public Page<PotObject> getPotListBySearch(PotSearchForm potSearchForm, Pageable pageable) {
        Page<Pot> pots = searchPots(potSearchForm, pageable);
        return pots.map(PotMapper::toPotObject);
    }

    @Transactional(readOnly = true)
    public Page<PotObject> getPotListBySearch(Authentication authentication, PotSearchForm potSearchForm, Pageable pageable) {
        Page<Pot> pots = searchPots(potSearchForm, pageable);
        Page<PotObject> potObjects = pots.map(PotMapper::toPotObject);

        // 찜 여부를 한 번의 쿼리로 채워 N+1 제거
        User user = userUtils.getUser(authentication);
        List<Long> potIds = potObjects.stream().map(p -> p.potId).collect(Collectors.toList());
        if (!potIds.isEmpty()) {
            Set<Long> likedPotIds = interestRepository.findByUser_IdAndPot_IdIn(user.getId(), potIds).stream()
                    .map(interest -> interest.getPot().getId())
                    .collect(Collectors.toSet());
            potObjects.forEach(p -> p.setWish(likedPotIds.contains(p.potId)));
        }
        return potObjects;
    }

    private Page<Pot> searchPots(PotSearchForm potSearchForm, Pageable pageable) {
        String searchText = potSearchForm.getSearchText();
        String searchType = potSearchForm.getSearchType();

        if (("remain".equals(searchType) || "term".equals(searchType)) && !isNumeric(searchText)) {
            throw new CustomException("숫자를 입력해주세요");
        }

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        return potRepositoryCustom.findListBySearch(potSearchForm, PageRequest.of(page, 8));
    }

    @Transactional(readOnly = true)
    public PotInfoObject getPot(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        boolean isMaster = pot.getMaster() != null && pot.getMaster().getId().equals(user.getId());
        Apply apply = applyRepository.findByPot_IdAndApplicant_Id(potId, user.getId());
        boolean isApprovedMember = apply != null && apply.getState() == State.APPROVED;
        return PotMapper.toPotInfoObject(pot, isMaster, isMaster || isApprovedMember);
    }

    @Transactional
    public void getPotStarted(Authentication authentication, Long potId, PotStartForm potStartForm) {
        Pot pot = getPotOwnedBy(authentication, potId);
        pot.changeState();
        pot.addAdditionalInfo(potStartForm);
        applyRepository.deleteByStateAndPot_Id(State.REJECTED, potId);
        applyRepository.deleteByStateAndPot_Id(State.APPLY, potId);
        potRepository.save(pot);
    }

    @Transactional(readOnly = true)
    public Pot getPotEntity(Long potId) {
        return potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
    }

    // 요청자가 해당 팟의 master인지 검증한 뒤 Pot을 반환
    private Pot getPotOwnedBy(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        if (pot.getMaster() == null || !pot.getMaster().getId().equals(user.getId())) {
            throw new AccessDeniedException("해당 팟에 대한 권한이 없습니다.");
        }
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
