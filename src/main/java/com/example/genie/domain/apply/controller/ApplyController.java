package com.example.genie.domain.apply.controller;
import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.exception.PotAlreadyFullException;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@AllArgsConstructor
@Controller
public class ApplyController {

    final UserUtils userUtils;
    final ApplyService applyService;
    final PotService potService;

    //팟에 가입 신청(신청 버튼 누르면 동작)
    @RequestMapping("/pot/apply")
    public String createApply(@RequestParam Long potId, Authentication authentication, Model model, RedirectAttributes redirectAttributes){
        User user = userUtils.getUser(authentication);
        Apply apply = applyService.createApply(user, potId);
        if(apply == null){ //이미 가입 신청한 팟
            redirectAttributes.addAttribute("alreadyApply", true);
            return "redirect:/pot/"+potId;
        }
        redirectAttributes.addAttribute("applySuccess", true);
        return "redirect:/pot/"+potId; //상세 페이지로 리다이렉트
    }

    @GetMapping("/pot/apply/users")
    //팟에 가입 신청한 유저 리스트
    public String getPotApplyList(@RequestParam Long potId, Model model){ //Pot 받아오는 방법은 추후 변경
        List<User> userList = applyService.getApplyUserList(potId);
        Pot pot = potService.getPotEntity(potId);
        List<User> memberList = applyService.getApprovedUserList(pot);
        model.addAttribute("pot", pot);
        model.addAttribute("userList", userList);
        model.addAttribute("memberList", memberList);
        return "pot/applyList";
    }

    //팟 가입 신청 처리 (기존 submitRequest 함수명 변경), 승인: 1, 거절:0
    @PostMapping("/pot/apply/approve")
    public String approveApply(@RequestParam Long potId, @RequestParam Long userId, @RequestParam int state, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes, Authentication authentication){
        //1. Apply 상태 변경
        try {
            applyService.appoveApply(potId, userId, state);
        } catch (PotAlreadyFullException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pot", potService.getPot(authentication, potId));
            return "pot/applyList";
        }
        redirectAttributes.addAttribute("potId", potId);
        return "redirect:/pot/apply/users";
    }
}
