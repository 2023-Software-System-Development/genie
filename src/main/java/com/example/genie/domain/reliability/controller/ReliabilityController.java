package com.example.genie.domain.reliability.controller;

import com.example.genie.domain.reliability.Service.ReliabilityService;
import com.example.genie.domain.reliability.model.ReliabilityInfoObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reliability")
public class ReliabilityController {

    private final ReliabilityService reliabilityService;

    //신뢰도 내역 확인 화면
    @GetMapping
    public String getReliability(Authentication authentication, Model model, @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<ReliabilityInfoObject> reliabilityInfoObjectList = reliabilityService.getUserReliabilities(authentication, pageable);
        model.addAttribute("reliabilityList", reliabilityInfoObjectList);
        return "reliability/reliability";
    }
}
