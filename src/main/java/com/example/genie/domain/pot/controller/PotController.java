package com.example.genie.domain.pot.controller;

import com.example.genie.domain.pot.form.PotCreateForm;
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

    @GetMapping
    public String createPotForm(HttpServletRequest request) {
        // 이전 페이지 URL 저장
        String previousUrl = request.getHeader("Referer");
        request.getSession().setAttribute("previousUrl", previousUrl);

        return "createPot";
    }

    @PostMapping
    public String createPot(Authentication authentication, @Valid @ModelAttribute PotCreateForm potCreateForm, BindingResult bindingResult, HttpServletRequest request, SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()){
            return "createPot";
        }
        potService.createPot(authentication, potCreateForm, bindingResult);

        // 이전 페이지 URL 가져오기
        String previousUrl = (String) request.getSession().getAttribute("previousUrl");
        sessionStatus.setComplete();

        // 이전 페이지로 리다이렉트
        return "redirect:" + previousUrl;
    }

    @PostMapping("/delete")
    public String deletePot(@RequestParam("potId") Long potId, SessionStatus sessionStatus) {
        potService.deletePot(potId);

        sessionStatus.setComplete();
        return "redirect:/user/login";
    }

    //메인페이지에서 보일 팟 리스트 화면
    @GetMapping("/list")
    public String getPotList(@RequestParam("ottType") String ottType, @PageableDefault(page = 0, size = 6) Pageable pageable,
                             Model model) {
        List<PotObject> potObjectList = potService.getPotList(ottType, pageable);
        model.addAttribute("potlist", potObjectList);
        return "main";
    }

    //팟 상세 정보 조회
    @GetMapping("/{potId}")
    public String getPot(@PathVariable Long potId, Model model){
        PotInfoObject potInfoObject = potService.getPot(potId);
        model.addAttribute("pot", potInfoObject);
        return "potInfo";
    }

}
