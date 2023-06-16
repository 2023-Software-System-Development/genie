package com.example.genie.domain.user.controller;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.model.PotInfoObject;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    final UserUtils userUtils;
    final UserService userService;


    @GetMapping("/info")
    public String getUserPage(Authentication authentication, Model model){
        User user = userUtils.getUser(authentication);
        model.addAttribute(user);
        return "myPage/mypage";
    }

    @GetMapping("/potList")
    public String getUserPotList(Authentication authentication, Model model){
        User user = userUtils.getUser(authentication);
        List<PotInfoObject> userPotList = userService.getUserPotList(user.getId());
        model.addAttribute("userPotList", userPotList);
        List<PotInfoObject> userApplyPotList = userService.getUserApplyPotList(user.getId());
        model.addAttribute("userApplyPotList", userApplyPotList);

        System.out.println("size: " + userApplyPotList.size());
        return "myPage/mypot_list";
    }

}
