package com.example.genie.domain.pot.controller;

import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.pot.entity.State;
import com.example.genie.domain.pot.form.PotCreateForm;
import com.example.genie.domain.pot.form.PotSearchForm;
import com.example.genie.domain.pot.form.PotStartForm;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.service.UserService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/pot")
@SessionAttributes("potSearchForm")
public class PotController {

    private final PotService potService;
    private final ApplyService applyService;
    private final UserService userService;

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
        return "redirect:/";
    }

    //메인페이지에서 보일 팟 리스트 조회 API
    @RequestMapping("/list")
    public String getPotList(@RequestParam(value = "ottType", required = false) String ottType, @ModelAttribute PotSearchForm potSearchForm, @PageableDefault(page = 0, size = 6) Pageable pageable,
                             Model model, Authentication authentication) {
        if(ottType != null){//만약 네비게이션에서 오티티 타입 클릭하면 potSearchForm 초기화
            potSearchForm.setOttType(ottType);
            potSearchForm.setSearchText(null);
            potSearchForm.setSearchType(null);
        }
        Page<PotObject> potObjectList;
        if(authentication != null)
            potObjectList = potService.getPotListBySearch(authentication, potSearchForm, pageable);
        else
            potObjectList = potService.getPotListBySearch(potSearchForm, pageable);
        model.addAttribute("potList", potObjectList);
        return "mainPage/home";
    }

    //팟 상세 정보 조회 API
    @GetMapping("/{potId}")
    public String getPot(Authentication authentication, @PathVariable Long potId, Model model){
        PotInfoObject potInfoObject = potService.getPot(authentication, potId);
        Apply apply = applyService.getApply(potId, authentication);
        Boolean isOngoing;
        model.addAttribute("pot", potInfoObject);
        isOngoing = (potInfoObject.getStartDate() != null) ? true : false;
        model.addAttribute("isOngoing", isOngoing);
        if(apply != null){
            model.addAttribute("state", apply.getState().toString());
        }

        return "pot/potInfo";
    }

    //팟 수정 API (모집중)
    @PostMapping("/edit/recruiting")
    public String editPotRecruiting(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotInfoObject potInfoObject, BindingResult bindingResult, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            return "pot/editRecruiting";
        }
        potService.editRecruitingPot(potId, potInfoObject);
        sessionStatus.setComplete();
        return "redirect:/pot/" + potId;
//        return "user/mypage";
    }

    //팟 수정 API (진행중)
    @PostMapping("/edit/ongoing")
    public String editPotOngoing(@RequestParam("potId") Long potId, @Valid @ModelAttribute PotInfoObject potInfoObject, BindingResult bindingResult, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            return "pot/editOngoing";
        }
        potService.editOngoingPot(potId, potInfoObject);
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
    @GetMapping("/{potId}/start")
    public String getPotStartedForm(@ModelAttribute PotStartForm potStartForm, @PathVariable Long potId, Authentication authentication, Model model) {
        PotInfoObject pot = potService.getPot(authentication, potId);
        model.addAttribute("pot", pot);
        return "pot/startPot";
    }

    //팟 시작 시, 추가 정보 입력
    @PostMapping("/{potId}/start")
    public String getPotStarted(@PathVariable("potId") Long potId, @Valid @ModelAttribute PotStartForm potStartForm, BindingResult bindingResult, Model model, Authentication authentication) {
        if(bindingResult.hasErrors()){
            model.addAttribute("pot", potService.getPot(authentication, potId));
            return "pot/startPot";
        }
        potService.getPotStarted(potId, potStartForm);
        return "redirect:/pot/" + potId +"/main";  //시작한 팟의 메인 페이지로 가도록 수정 필요
    }

    @GetMapping("/{potId}/main")
    public String potMain(@PathVariable Long potId, Authentication authentication, Model model){
        PotInfoObject pot = potService.getPot(authentication, potId);
        model.addAttribute("pot", pot);
        return "/pot/potMain";
    }

    @GetMapping("/{potId}/chat")
    public String potChat(@PathVariable Long potId, Authentication authentication, Model model){
        String username = userService.getUserNickName(authentication);
        model.addAttribute("username", username);
        return "/chat/chatMain";
    }
}
