package com.example.genie.domain.apply.controller;
import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.exception.PotAlreadyFullException;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String createApply(@RequestParam Long potId, Authentication authentication){
        User user = userUtils.getUser(authentication);
        Apply apply = applyService.createApply(user, potId);
        if(apply == null){
            return "detail";
        }
        return "redirect:/"; //성공하면 메인 화면으로 돌아감
    }

    @GetMapping("/pot/apply/users")
    //팟에 가입 신청한 유저 리스트
    public String getPotApplyList(@RequestParam Long potId, Model model){ //Pot 받아오는 방법은 추후 변경
        List<User> userList = applyService.getApplyUserList(potId);
        model.addAttribute("userList", userList);
        return "applyList";
    }

    //팟 가입 신청 처리 (기존 submitRequest 함수명 변경), 승인: 1, 거절:0
    @GetMapping("/pot/apply/approve")
    public String appoveApply(@RequestParam Long potId, @RequestParam Long userId, @RequestParam int state, HttpServletRequest request, Model model){
        //1. Apply 상태 변경

        try {
            applyService.appoveApply(potId, userId, state);
        } catch (PotAlreadyFullException e) {
            model.addAttribute("error", e.getMessage());
            return "applyList";
        }
        return "detail";
    }
}
