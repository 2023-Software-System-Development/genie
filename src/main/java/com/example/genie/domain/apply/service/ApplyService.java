package com.example.genie.domain.apply.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.exception.PotAlreadyFullException;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.repository.PotRepository;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final PotRepository potRepository;
    private final UserUtils userUtils;

    //Apply 생성
    @Transactional
    public Apply createApply(User user, Long potId){
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        // 자신이 만든 팟에는 신청 불가
        if (pot.getMaster() != null && pot.getMaster().getId().equals(user.getId())) {
            return null;
        }
        if(getApply(potId, user.getId())!=null){ //이미 가입 신청한 팟
            return null;
        }
        Apply apply = Apply.builder().state(State.APPLY).applicant(user).pot(pot).build();
        applyRepository.save(apply);
        return apply;
    }

    @Transactional(readOnly = true)
    public Apply getApply(Long potId, Long userId){
        return applyRepository.findByPot_IdAndApplicant_Id(potId, userId);
    }

    @Transactional(readOnly = true)
    public Apply getApply(Long potId, Authentication authentication){
        User user = userUtils.getUser(authentication);
        return applyRepository.findByPot_IdAndApplicant_Id(potId, user.getId());
    }

    @Transactional(readOnly = true)
    public List<User> getApplyUserList(Long potId){
        List<Apply> applyList = applyRepository.findByStateAndPot_Id(State.APPLY, potId);
        List<User> userList = new ArrayList<>();
        for(Apply apply : applyList){
            userList.add(apply.getApplicant());
        }
        return userList;
    }

    // 가입 신청 처리. 해당 팟의 master 본인만 가능. 승인:1, 그 외:거절
    @Transactional
    public void approveApply(Long potId, Long userId, int state, Authentication authentication) throws PotAlreadyFullException {
        // 동시 승인으로 인한 정원 초과(lost update)를 막기 위해 행 잠금으로 Pot 조회
        Pot pot = potRepository.findByIdForUpdate(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));

        // master 본인만 승인/거절 가능
        User requester = userUtils.getUser(authentication);
        if (pot.getMaster() == null || !pot.getMaster().getId().equals(requester.getId())) {
            throw new AccessDeniedException("해당 팟에 대한 권한이 없습니다.");
        }

        Apply apply = applyRepository.findByPot_IdAndApplicant_Id(potId, userId);
        if (apply == null) {
            throw new EntityNotFoundException("가입 신청 내역이 없습니다.");
        }

        State s = (state == 1 ? State.APPROVED : State.REJECTED);
        // 정원 체크는 '승인'에만 적용 (거절은 정원과 무관하게 가능)
        if (s == State.APPROVED) {
            if (pot.getRemain() <= 0) {
                throw new PotAlreadyFullException(pot.getPotName());
            }
            pot.approveUser();
        }
        apply.changeState(s);
        applyRepository.save(apply);
    }

    @Transactional(readOnly = true)
    public List<User> getApprovedUserList(Pot pot){
        List<Apply> applyList = applyRepository.findByStateAndPot_Id(State.APPROVED, pot.getId());
        List<User> userList = new ArrayList<>();
        for(Apply apply : applyList){
            userList.add(apply.getApplicant());
        }
        return userList;
    }
}
