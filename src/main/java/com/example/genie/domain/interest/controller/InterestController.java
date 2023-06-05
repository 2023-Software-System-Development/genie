package com.example.genie.domain.interest.controller;

import com.example.genie.domain.interest.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {

    private final InterestService interestService;

    @PostMapping
    public void saveInterest(Authentication authentication, @RequestParam("potId") Long potId) {
        interestService.saveInterest(authentication, potId);
    }
}
