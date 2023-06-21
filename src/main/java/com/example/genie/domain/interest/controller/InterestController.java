package com.example.genie.domain.interest.controller;

import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.interest.service.InterestService;
import com.example.genie.domain.pot.model.PotObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InterestController {

    private final InterestService interestService;
    @GetMapping("/user/interestList")
    public String getUserInterestList(Authentication authentication, Model model, @PageableDefault(page = 1, size = 6) Pageable pageable){
        Page<PotObject> interestPotList = interestService.getUserInterestPotList(authentication, pageable);
        model.addAttribute("interestPotList", interestPotList);
        return "myPage/heart";
    }

    @PostMapping("/interest/save/{potId}")
    @ResponseBody
    public Long saveInterest(Authentication authentication, @PathVariable Long potId) {
        Interest interest = interestService.saveInterest(authentication, potId);
        return interest.getId();
    }

    @DeleteMapping("/interest/delete/{potId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String deleteInterest(@PathVariable Long potId, Authentication authentication) {
        interestService.deleteInterest(authentication, potId);
        return "ajax :: #resultDiv";
    }

}
