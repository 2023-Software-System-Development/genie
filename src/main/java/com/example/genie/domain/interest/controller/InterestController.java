package com.example.genie.domain.interest.controller;

import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.interest.service.InterestService;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.pot.service.PotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InterestController {

    private final InterestService interestService;
    private final PotService potService;

    @GetMapping("/user/interestList")
    public String getUserInterestList(Authentication authentication, Model model){
        List<Interest> interestList = interestService.getUserInterestList(authentication);
        model.addAttribute("interestList", interestList);
        return "myPage/heart";
    }

    @PostMapping("/interest/save/{potId}")
    @ResponseBody
    public Long saveInterest(Authentication authentication, @PathVariable Long potId) {
        Interest interest = interestService.saveInterest(authentication, potId);

        return interest.getId();
    }

    @DeleteMapping("/interest/delete/{potId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteInterest(@PathVariable Long potId, Authentication authentication) {
        interestService.deleteInterest(authentication, potId);
        return "ajax :: #resultDiv";
    }
}
