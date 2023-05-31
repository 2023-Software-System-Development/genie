package com.example.genie.domain.apply.controller;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ApplyController {
    @Autowired
    UserUtils userUtils;
    ApplyService applyService;
    PotService potService;

    //팟에 가입 신청(신청 버튼 누르면 동작)
    @RequestMapping("/pot/apply")
    public String createApply(@RequestParam String potId, Authentication authentication){
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPot(potId);
        Apply apply = applyService.createApply(user, pot);
        if(apply == null){
            return "detail";
        }
        return "redirect:/"; //성공하면 메인 화면으로 돌아감
    }

    @GetMapping("/pot/apply/users")
    //팟에 가입 신청한 유저 리스트
    public String getPotApplyList(@RequestParam String potId, Model model){ //Pot 받아오는 방법은 추후 변경
        List<User> userList = applyService.getApplyUserList(potId);
        model.addAttribute("userList", userList);
        return "applyList";
    }

    //팟 가입 신청 처리 (기존 submitRequest 함수명 변경), 승인: 1, 거절:0
    @GetMapping("/pot/apply/approve")
    public String appoveApply(@RequestParam String potId, @RequestParam String userId, @RequestParam int state){
        //1. Apply 상태 변경
        Apply apply = applyService.appoveApply(potId, userId, state);
        return "detail";
    }
}
