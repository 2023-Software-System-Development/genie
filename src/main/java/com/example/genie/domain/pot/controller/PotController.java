package com.example.genie.domain.pot.controller;

import com.example.genie.domain.pot.entity.State;
import com.example.genie.domain.pot.form.*;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.service.PotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@SessionAttributes("potSearchForm")
public class PotController {

    private final PotService potService;

    @ModelAttribute
    public PotSearchForm potSearchForm(){
        return new PotSearchForm();
    }


    //팟 생성 폼을 호출하는 API
    @GetMapping
    public String createPotForm(HttpServletRequest request, @ModelAttribute PotCreateForm potCreateForm, Model model) {
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
    @RequestMapping("/list")
    public String getPotList(@RequestParam(value = "ottType", required = false) String ottType, @ModelAttribute PotSearchForm potSearchForm, @PageableDefault(page = 0, size = 6) Pageable pageable,
                             Model model) {
        if(ottType != null){//만약 네비게이션에서 오티티 타입 클릭하면 potSearchForm 초기화
            potSearchForm.setOttType(ottType);
            potSearchForm.setSearchText(null);
            potSearchForm.setSearchType(null);
        }
        Page<PotObject> potObjectList = potService.getPotListBySearch(potSearchForm, pageable);
        model.addAttribute("potList", potObjectList);
        return "mainPage/home";
    }

    //팟 상세 정보 조회 API
    @GetMapping("/{potId}")
    public String getPot(Authentication authentication, @PathVariable Long potId, Model model){
        PotInfoObject potInfoObject = potService.getPot(authentication, potId);
        model.addAttribute("pot", potInfoObject);
        return "pot/potInfo";
    }

    //팟 수정 API (모집중)
    @PostMapping("/edit/recruiting")
    public String editPot(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotEditRecruitingForm potEditRecruitingForm, BindingResult bindingResult, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            return "pot/editRecruiting";
        }
        potService.editRecruitingPot(potId, potEditRecruitingForm);
        sessionStatus.setComplete();
        return "redirect:/pot/" + potId;
//        return "user/mypage";
    }

    //팟 수정 API (진행중)
    @PostMapping("/edit/ongoing")
    public String editPot(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotEditOngoingForm potEditOngoingForm, BindingResult bindingResult, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            return "pot/editOngoing";
        }
        potService.editOngoingPot(potId, potEditOngoingForm);
        sessionStatus.setComplete();
        return "redirect:/pot/" + potId;
//        return "user/mypage";
    }

    //팟 수정 화면 호출 API. 상태에 따라 보내는 화면 다르게
    @GetMapping("/edit")
    public String editPotForm(Authentication authentication, @RequestParam("potId") Long potId, Model model) {
        PotInfoObject potInfoObject = potService.getPot(authentication, potId);
        if(potService.getPotEntity(potId).getState().equals(State.RECRUITING)) {
            model.addAttribute("pot", potInfoObject); //아직 회원 모집 중일때
            return "pot/editRecruiting";
        }
        else {
            model.addAttribute("pot", potInfoObject); //
            return "pot/editOngoing";
        }
    }

    //팟 시작 시, 추가 정보 입력 화면 호출 API
    @GetMapping("/start")
    public String getPotStartedForm() {
        return "pot/startPot";
    }

    //팟 시작 시, 추가 정보 입력
    @PostMapping("/start")
    public String getPotStarted(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotStartForm potStartForm,BindingResult bindingResult) {
        potService.getPotStarted(potId, potStartForm);
        return "redirect:/pot/" + potId;
    }

}
