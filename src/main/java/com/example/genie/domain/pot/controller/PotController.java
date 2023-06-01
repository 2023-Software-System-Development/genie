package com.example.genie.domain.pot.controller;

import com.example.genie.domain.pot.entity.State;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.pot.form.PotEditOngoingForm;
import com.example.genie.domain.pot.form.PotEditRecruitingForm;
import com.example.genie.domain.pot.form.PotSearchForm;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.service.PotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pot")
public class PotController {

    private final PotService potService;

    //팟 생성 폼을 호출하는 API
    @GetMapping
    public String createPotForm(HttpServletRequest request) {
        // 이전 페이지 URL 저장
        String previousUrl = request.getHeader("Referer");
        request.getSession().setAttribute("previousUrl", previousUrl);

        return "pot/createPot";
    }

    //팟 생성 API
    @PostMapping
    public String createPot(Authentication authentication, @Valid @ModelAttribute PotCreateForm potCreateForm, BindingResult bindingResult, HttpServletRequest request, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            return "pot/createPot";
        }
        potService.createPot(authentication, potCreateForm, bindingResult);

        // 이전 페이지 URL 가져오기
        String previousUrl = (String) request.getSession().getAttribute("previousUrl");
        sessionStatus.setComplete();

        // 이전 페이지로 리다이렉트
        return "redirect:" + previousUrl;
    }

    //팟 삭제 API
    @PostMapping("/delete")
    public String deletePot(@RequestParam("potId") Long potId, SessionStatus sessionStatus) {
        potService.deletePot(potId);

        sessionStatus.setComplete();
        return "user/myPage";
    }

    //메인페이지에서 보일 팟 리스트 조회 API
    @GetMapping("/list")
    public String getPotList(@RequestParam("ottType") String ottType, @PageableDefault(page = 0, size = 6) Pageable pageable,
                             Model model) {
        List<PotObject> potObjectList = potService.getPotList(ottType, pageable);
        model.addAttribute("potlist", potObjectList);
        return "main";
    }

    //팟 상세 정보 조회 API
    @GetMapping("/{potId}")
    public String getPot(@PathVariable Long potId, Model model){
        PotInfoObject potInfoObject = potService.getPot(potId);
        model.addAttribute("pot", potInfoObject);
        return "pot/potInfo";
    }

    //팟 수정 API (모집중)
    @PostMapping("/edit/recruting")
    public String editPot(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotEditRecruitingForm potEditRecruitingForm, SessionStatus sessionStatus) {
        potService.editRecruitingPot(potId, potEditRecruitingForm);
        sessionStatus.setComplete();
        return "user/mypage";
    }

    //팟 수정 API (진행중)
    @PostMapping("/edit/ongoing")
    public String editPot(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotEditOngoingForm potEditOngoingForm, SessionStatus sessionStatus) {
        potService.editOngoingPot(potId, potEditOngoingForm);
        sessionStatus.setComplete();
        return "user/mypage";
    }

    //팟 수정 화면 호출 API. 상태에 따라 보내는 화면 다르게
    @GetMapping("/edit")
    public String editPotForm(@RequestParam("potId") Long potId, Model model) {
        PotInfoObject potInfoObject = potService.getPot(potId);
        if(potService.getPotEntity(potId).getState().equals(State.RECRUITING)) {
            model.addAttribute("pot", potInfoObject);
            return "pot/editRecruiting";
        }
        else {
            model.addAttribute("pot", potInfoObject);
            return "pot/editOngoing";
        }
    }

    @PostMapping("/start")
    public String getPotStarted(@RequestParam("potId") Long potId, Model model) {
        PotInfoObject potInfoObject = potService.getPotStarted(potId);
        model.addAttribute("pot", potInfoObject);
        return "pot/potInfo";
    }

    @PostMapping("/search")
    public String searchPot(@Valid @ModelAttribute PotSearchForm potSearchForm, @PageableDefault(page = 0, size = 6) Pageable pageable, Model model) {
        List<PotObject> potObjectList = potService.getPotListBySearch(potSearchForm, pageable);
        model.addAttribute("potlist", potObjectList);
        return "main";
    }


}
