package com.example.genie.domain.apply.controller;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.auth.service.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
public class ApplyController {

    @Autowired
    ApplyService applyService;
    @RequestMapping("/pot/apply") //팟 가입 신청
    public String apply(@ModelAttribute Long potId, @AuthenticationPrincipal Object principal){
        CustomUserDetails userDetails = (CustomUserDetails)principal;
        Long userId = ((CustomUserDetails) principal).getId();
        Apply apply = applyService.createApply(potId, userId);

        if(apply == null)
            return "/pot/detail";
        return "/";
    }

    @RequestMapping("/pot/apply/users")
    public String getApplyList(){
        List<Apply> applies = ApplyService.
    }
}
